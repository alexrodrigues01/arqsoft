package com.isep.acme.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.acme.model.VoteRedis;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;
import java.util.List;

public class ListOfVotesRedisConverter implements RedisSerializer<List<VoteRedis>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(List<VoteRedis> votes) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(votes);
        } catch (IOException e) {
            throw new SerializationException("Error serializing List of VoteRedis", e);
        }
    }

    @Override
    public List<VoteRedis> deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, new TypeReference<List<VoteRedis>>() {});
        } catch (IOException e) {
            throw new SerializationException("Error deserializing List of VoteRedis", e);
        }
    }
}
