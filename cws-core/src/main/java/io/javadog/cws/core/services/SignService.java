/*
 * =============================================================================
 * Copyright (c) 2016-2018, JavaDog.io
 * -----------------------------------------------------------------------------
 * Project: CWS (cws-core)
 * =============================================================================
 */
package io.javadog.cws.core.services;

import io.javadog.cws.api.requests.SignRequest;
import io.javadog.cws.api.responses.SignResponse;
import io.javadog.cws.core.enums.Permission;
import io.javadog.cws.core.model.Settings;
import io.javadog.cws.core.model.entities.SignatureEntity;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author Kim Jensen
 * @since  CWS 1.0
 */
public final class SignService extends Serviceable<SignResponse, SignRequest> {

    public SignService(final Settings settings, final EntityManager entityManager) {
        super(settings, entityManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SignResponse perform(final SignRequest request) {
        // Pre-checks, & destruction of credentials
        verifyRequest(request, Permission.CREATE_SIGNATURE);
        Arrays.fill(request.getCredential(), (byte) 0);

        final SignResponse response = new SignResponse();

        final byte[] rawSignature = crypto.sign(keyPair.getPrivate().getKey(), request.getData());
        final String signature = Base64.getEncoder().encodeToString(rawSignature);
        final String checksum = crypto.generateChecksum(rawSignature);
        final SignatureEntity existing = dao.findByChecksum(checksum);

        if (existing == null) {
            final SignatureEntity entity = new SignatureEntity();
            entity.setPublicKey(member.getPublicKey());
            entity.setMember(member);
            entity.setChecksum(checksum);
            entity.setExpires(request.getExpires());
            dao.persist(entity);
        } else {
            response.setReturnMessage("This document has already been signed.");
        }

        response.setSignature(signature);
        return response;
    }
}
