package com.chatterjee.sayan.payzapp.payment.simulator;

import com.chatterjee.sayan.payzapp.common.enums.PaymentStatus;
import com.chatterjee.sayan.payzapp.payment.entities.Payment;
import com.chatterjee.sayan.payzapp.payment.repositories.PaymentRepository;
import com.chatterjee.sayan.payzapp.payment.services.PaymentService;
import com.chatterjee.sayan.payzapp.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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

    }


}
