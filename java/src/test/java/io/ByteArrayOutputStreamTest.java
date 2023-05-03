package io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ByteArrayOutputStream 클래스 테스트
 */
public class ByteArrayOutputStreamTest {

    private final byte[] inSrc = {1, 2, 3};

    @Test
    @DisplayName("nullOutputStream() 테스트")
    void nullOutputStream() throws IOException {
    }

    @Test
    @DisplayName("write(int b) 테스트")
    void write1Test() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(2);
        output.write(3);

        assertEquals(Arrays.toString(output.toByteArray()),
                Arrays.toString(new byte[]{2, 3}));
    }

    @Test
    @DisplayName("write(byte[] b, int off, int len) 테스트")
    void write2Test() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(inSrc, 0, 3);
        output.write(inSrc, 1, 2);

        assertEquals(Arrays.toString(output.toByteArray()),
                Arrays.toString(new byte[]{1, 2, 3, 2, 3}));
    }

    @Test
    @DisplayName("write(byte[] b, int off, int len)에서 off나 len이 범위를 벗어난 경우")
    void write2ExceptionTest() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        assertThrows(IndexOutOfBoundsException.class,
                () -> output.write(inSrc, -1, 3));
        assertThrows(IndexOutOfBoundsException.class,
                () -> output.write(inSrc, 4, 3));
        assertThrows(IndexOutOfBoundsException.class,
                () -> output.write(inSrc, 0, -1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> output.write(inSrc, 0, 4));
    }

    @Test
    @DisplayName("write(byte[] b) 테스트")
    void write3Test() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(inSrc);
        output.write(new byte[]{4, 5, 6});

        assertEquals(Arrays.toString(output.toByteArray()),
                Arrays.toString(new byte[]{1, 2, 3, 4, 5, 6}));
    }

    @Test
    @DisplayName("writeBytes(byte[] b) 테스트")
    void writeBytesTest() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.writeBytes(inSrc);

        assertEquals(Arrays.toString(output.toByteArray()), Arrays.toString(inSrc));
    }

    @Test
    @DisplayName("writeTo(OutputSteam out) 테스트")
    void writeToTest() throws IOException {
        ByteArrayOutputStream output1 = new ByteArrayOutputStream();
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();

        output1.writeBytes(inSrc);
        output1.writeTo(output2);

        assertEquals(Arrays.toString(output2.toByteArray()), Arrays.toString(inSrc));
    }

    @Test
    @DisplayName("reset() 테스트")
    void resetTest() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(inSrc);
        output.reset();

        assertEquals(Arrays.toString(output.toByteArray()),
                Arrays.toString(new byte[0]));
        assertEquals(output.size(), 0);
    }

    /**
     * ByteArrayOutputStream의 경우 사용하는 자원이 메모리 밖에 없어 가비지컬렉터에 의해 자동적으로
     * 자원을 반환하므로 close()를 이용하지 않아도 된다.
     */
    @Test
    @DisplayName("close() 테스트")
    void closeTest() {
    }

    @Test
    @DisplayName("size() 테스트")
    void sizeTest() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(1);
        assertEquals(output.size(), 1);
        output.write(2);
        assertEquals(output.size(), 2);
        output.write(3);
        assertEquals(output.size(), 3);
    }

    /**
     * ByteArrayOutputStream에선 따로 재정의하여 사용하지 않기 때문에 flush()는 아무 처리를 하지
     * 않는다.
     */
    @Test
    @DisplayName("flush() 테스트")
    void flushTest() {
    }

    @Test
    @DisplayName("toByteArray() 테스트")
    void toByteArrayTest() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(1);
        output.write(2);

        byte[] result = output.toByteArray();

        assertEquals(result[1], 2);
    }

    /**
     * toString()의 경우 defaultCharset을 이용하여 바이트를 문자로 변환한다.
     * defaultCharset은 JVM 실행 과정에서 정해지며 운영체제에 의존적인이다.
     * @throws IOException
     */
    @Test
    @DisplayName("toString() 테스트")
    void toString1Test() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(inSrc);

        assertEquals(output.toString(), "\u0001\u0002\u0003");
    }

    @Test
    @DisplayName("toString(Charset charset) 테스트")
    void toString2Test() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Charset charset = StandardCharsets.US_ASCII;

        output.write(inSrc);

        assertEquals(output.toString(charset), "\u0001\u0002\u0003");
    }

    @Test
    @DisplayName("toString(String charsetName) 테스트")
    void toString3Test() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(inSrc);

        assertEquals(output.toString("UTF-16LE"), "ȁ�");
    }
}
