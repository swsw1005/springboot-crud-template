package kr.co.demojar;


import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class SqlGenarateTest {

    public static final String[] WRITERS = {
            "임성우", "홍길동", "김철수", "박정수", "최길훈",
            "유재석", "박명수", "정준하", "정형돈", "노홍철",
            "장영실", "이순신", "이성계", "유관순", "안중근",
    };

    public static final String[] TEXTS = {
            "안녕하세요",
            "반갑습니다",
            "안녕히 계세요",
            "hello world",
            "커피 한잔 할래요",
            "새로 산 노트북",
            "날씨가 더워요",
    };

    @Test
    public void test01() throws ParseException {

        final int writerCnt = WRITERS.length;
        final int textCnt = TEXTS.length;

        final String sql1 = "insert into tb_board values ( " +
                " '%s' , '%s' , '%s' , '%s' , '%s' " +
                ");";

        String startDate = "2023-01-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = sdf.parse(startDate);

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(date);

        Calendar endCal = Calendar.getInstance();

        final long totalInterval = endCal.getTimeInMillis() - startCal.getTimeInMillis();

        final int sampleCnt = 2000;

        final long interval = totalInterval / sampleCnt;

        for (int i = 0; i < sampleCnt; i++) {

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(startCal.getTimeInMillis() + interval * i);

            final String id = UUID.randomUUID().toString();

            int writerIdx = (int) (Math.random() * 100) % writerCnt;
            final String writer = WRITERS[writerIdx];

            int textLines = (int) (Math.random() * 100) % 10 + 1;
            StringBuilder bodyBuilder = new StringBuilder();
            for (int j = 0; j < textLines; j++) {
                int textIdx = (int) (Math.random() * 100) % textCnt;
                bodyBuilder.append(TEXTS[textIdx]).append("\\n");
            }
            final String body = bodyBuilder.toString();

            int titleLines = (int) (Math.random() * 100) % 3 + 1;
            StringBuilder titleBuilder = new StringBuilder();
            for (int j = 0; j < titleLines; j++) {
                int textIdx = (int) (Math.random() * 100) % textCnt;
                titleBuilder.append(TEXTS[textIdx]).append(" _ ");
            }
            final String title = titleBuilder.toString();

            final String createdAt = sdf2.format(cal.getTime());

            System.out.println(String.format(sql1, id, title, body, createdAt, writer));

        }


    }


}
