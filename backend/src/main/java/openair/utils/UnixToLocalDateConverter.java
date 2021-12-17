package openair.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.TimeZone;

public class UnixToLocalDateConverter extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String unixTimestamp = p.getText().trim();
        return LocalDate.ofInstant(Instant.ofEpochMilli(Long.valueOf(unixTimestamp)), TimeZone.getDefault().toZoneId());
    }

}
