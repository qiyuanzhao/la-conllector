package com.lavector.collector.crawler.project.yanying.redbook;

import com.google.common.io.FileWriteMode;
import com.lavector.collector.crawler.nonce.NonceMessage;
import com.lavector.collector.crawler.util.JsonMapper;
import com.lavector.collector.crawler.util.StringToDateConverter;
import org.apache.commons.lang3.time.DateUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created on 22/06/2018.
 *
 * @author zeng.zhao
 */
public class RedBookDataFilter {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get("/Users/zeng.zhao/Desktop/red_book.json"));
        JsonMapper mapper = JsonMapper.buildNormalBinder();
        StringToDateConverter converter = new StringToDateConverter();
        LocalDate localDate = LocalDate.of(2018, 3, 20);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
        Date startTime = Date.from(zonedDateTime.toInstant());
        String line = reader.readLine();
        while (line != null) {
            NonceMessage message = mapper.fromJson(line, NonceMessage.class);
            Date date = converter.convert(message.getTime());
            if (date.after(startTime)) {
                String json = mapper.toJson(message);
                com.google.common.io.Files.asCharSink(Paths.get("/Users/zeng.zhao/Desktop/red_book_filter.json").toFile(),
                        StandardCharsets.UTF_8, FileWriteMode.APPEND)
                        .write(json + "\n");
            }
            line = reader.readLine();
        }
    }
}
