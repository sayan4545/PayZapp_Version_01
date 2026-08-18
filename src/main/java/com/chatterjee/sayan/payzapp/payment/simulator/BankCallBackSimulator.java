package com.chatterjee.sayan.payzapp.payment.simulator;

import com.chatterjee.sayan.payzapp.common.enums.ChaosMode;
import com.chatterjee.sayan.payzapp.common.enums.PaymentStatus;
import com.chatterjee.sayan.payzapp.common.utils.RandomizerUtil;
import com.chatterjee.sayan.payzapp.payment.entities.Payment;
import com.chatterjee.sayan.payzapp.payment.repositories.PaymentRepository;
import com.chatterjee.sayan.payzapp.payment.services.PaymentService;
import com.chatterjee.sayan.payzapp.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor

public class BankCallBackSimulator {

    private final PaymentRepository paymentRepository;
    private final VaultService vaultService;
    private final PaymentService paymentService;
    private final SimulatorConfig simulatorConfig;
    @Scheduled(fixedDelayString = "${payment.simulator.poll-interval-ms:5000}")
    public void processCallBacks(){
        LocalDateTime globalWindow = LocalDateTime.now().minusSeconds(1);
        List<Payment> candidates = paymentRepository.findByPaymentStatusAndCreatedAtBefore(PaymentStatus.AUTHORIZING,globalWindow);

        if(candidates.isEmpty()) return;
        for(Payment payment : candidates){
            simulateCallBack(payment);
        }
    }

    private void simulateCallBack(Payment payment){
        SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig = simulatorConfig.configFor(payment.getPaymentMethod());
        LocalDateTime dueAt = dueAt(payment,methodSimulatorConfig);

        if(LocalDateTime.now().isBefore(dueAt)){
            return;
        }

        ChaosMode chaosMode = simulatorConfig.getChaosMode();

        switch (chaosMode){
            case SUCCEED -> resolve(payment,true);
            case FAIL -> resolve(payment,false);
            case TIMEOUT -> {
                log.debug("Bank callback simulator: payment timed out");
            }
            case NORMAL,SLOW ->  shouldApprove(payment,methodSimulatorConfig);
        }


    }

    private void resolve(Payment payment , boolean approve){
        if(approve){
            String bankRef = "SIM_BANK_REF"+ RandomizerUtil.randomBase64(8);
            paymentService.resolveAuthorization(payment.getId(),true,bankRef,null,null);
        }
        else{
            paymentService.resolveAuthorization(payment.getId(),false,null,"SIM_BANK_ERROR_CODE","Simulated error description");

        }

    }

    private boolean shouldApprove(Payment payment, SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig){
        int bucket = Math.abs(payment.getId().hashCode())%100;
        return bucket < methodSimulatorConfig.getSuccessRate();
    }

    private LocalDateTime dueAt(Payment payment,SimulatorConfig.MethodSimulatorConfig methodSimulatorConfig){
        int range = methodSimulatorConfig.getMaxDelaySeconds() - methodSimulatorConfig.getMinDelaySeconds();
        int delaySeconds = methodSimulatorConfig.getMinDelaySeconds() + Math.abs(payment.getId().hashCode()) % (range+1);
        if(simulatorConfig.getChaosMode()== ChaosMode.SLOW){
            delaySeconds = delaySeconds*2;
        }
        return payment.getCreatedAt().plusSeconds(delaySeconds);

    }


}
