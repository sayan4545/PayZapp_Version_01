package com.chatterjee.sayan.payzapp.vault.service.impl;

import com.chatterjee.sayan.payzapp.common.enums.CardBrand;
import com.chatterjee.sayan.payzapp.common.utils.RandomizerUtil;
import com.chatterjee.sayan.payzapp.vault.config.VaultEncryptionConfig;
import com.chatterjee.sayan.payzapp.vault.dto.request.TokenizeRequest;
import com.chatterjee.sayan.payzapp.vault.dto.response.TokenizeResponse;
import com.chatterjee.sayan.payzapp.vault.entities.CardToken;
import com.chatterjee.sayan.payzapp.vault.entities.VaultCard;
import com.chatterjee.sayan.payzapp.vault.repository.CardTokenRepository;
import com.chatterjee.sayan.payzapp.vault.repository.VaultCardRepository;
import com.chatterjee.sayan.payzapp.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final VaultCardRepository vaultCardRepository;
    private final CardTokenRepository cardTokenRepository;
    private final BytesEncryptor bytesEncryptor;


    @Override
    @Transactional
    public TokenizeResponse tokenize(TokenizeRequest tokenizeRequest, UUID merchantId) {
        String lastFour = tokenizeRequest.pan()
                .substring(tokenizeRequest.pan().length() - 4);

        String bin = tokenizeRequest.pan()
                .substring(0,6);

        CardBrand cardBrand = DetectBrand(tokenizeRequest.pan());

        byte[] dek = KeyGenerators.secureRandom(32).generateKey();
        byte[] encryptedPan = VaultEncryptionConfig.panEncryptor(dek)
                .encrypt(tokenizeRequest.pan().getBytes(StandardCharsets.UTF_8));

        byte[] encryptedDek = bytesEncryptor.encrypt(dek);

        VaultCard vaultCard = VaultCard
                .builder()
                .cardHolderName(tokenizeRequest.cardHolderName())
                .brand(cardBrand)
                .encryptedDek(encryptedDek)
                .encryptedPan(encryptedPan)
                .lastFour(lastFour)
                .bin(bin)
                .expiryYear(tokenizeRequest.expiryYear().toString())
                .expiryMonth(tokenizeRequest.expiryMonth().toString())
                .build();
        vaultCardRepository.save(vaultCard);

        String token = "tok_"+ RandomizerUtil.randomBase64(16);
        CardToken cardToken = CardToken
                .builder()
                .vaultCard(vaultCard)
                .token(token)
                .customer(tokenizeRequest.customerId())
                .merchant(merchantId)
                .build();
        cardTokenRepository.save(cardToken);


        return new TokenizeResponse(token,lastFour,cardBrand,tokenizeRequest.expiryMonth(),tokenizeRequest.expiryYear());


    }

    private CardBrand DetectBrand(String pan) {
        if(pan.startsWith("4")) return CardBrand.VISA;
        if(pan.startsWith("5")||pan.startsWith("2")) return CardBrand.MASTERCARD;
        if(pan.startsWith("34")||pan.startsWith("37")) return CardBrand.AMEX;
        return CardBrand.RUPAY;

    }
}
