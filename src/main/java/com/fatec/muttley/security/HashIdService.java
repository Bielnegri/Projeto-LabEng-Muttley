package com.fatec.muttley.security;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HashIdService {

    private final Hashids hashids;

    public HashIdService(@Value("${hashids.secret}") String secret) {
        this.hashids = new Hashids(secret, 8);
    }

    public String encode(long id) {
        return hashids.encode(id);
    }

    public long decode(String hash) {
        long[] ids = hashids.decode(hash);
        if (ids.length == 0) throw new IllegalArgumentException("Hash inválido");
        return ids[0];
    }
}