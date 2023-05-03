package io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * ByteArrayInputStream 클래스 테스트
 */
public class ByteArrayInputStreamTest {

    private final byte[] inSrc = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private final String inSrcString = Arrays.toString(inSrc);

    @Test
    @DisplayName("nullInputStream() 테스트")
    void nullInputStreamTest() throws IOException {
        InputStream input = ByteArrayInputStream.nullInputStream();

        assertEquals(input.available(), 0);
        assertEquals(input.read(), -1);
    }

    @Test
    @DisplayName("read() 테스트")
    void read1Test() {
        ByteArrayInputStream input = new ByteArrayInputStream(inSrc);

        assertEquals(input.read(), 2);
        assertEquals(input.read(), 3);
    }

    /**
     * off는 b 배열에서 저장할 시작 위치를 의미하며, len은 저장 개수를 의미한다.
     */
    @Test
    @DisplayName("read(byte[] b, int off, int len) 테스트")
    void read2Test() {
        byte[] temp1 = new byte[4];
        byte[] temp2 = new byte[10];

        ByteArrayInputStream input1 = new ByteArrayInputStream(inSrc);
        ByteArrayInputStream input2 = new ByteArrayInputStream(inSrc);

        input1.read(temp1, 0, temp1.length);

        assertEquals(Arrays.toString(new byte[]{2, 3, 4, 5}), Arrays.toString(temp1));

        //이전 과정에서 read 메서드를 사용하여 4바이트를 읽어왔기 때문에 다시 read 메서드를 사용하는 경우
        //inSrc의 4번째 인덱스부터 값을 읽게 된다.
        input1.read(temp1, 2, temp1.length - 2);

        assertEquals(Arrays.toString(new byte[]{2, 3, 6, 7}), Arrays.toString(temp1));

        input2.read(temp2, 0, temp2.length);

        assertEquals(inSrcString, Arrays.toString(temp2));

        //이전 과정에서 read 메서드를 사용하여 10바이트를 읽어왔기 때문에 다시 read 메서드를 사용하는 경우
        //더 이상 읽을 바이트가 없기 때문에 temp2 배열에 대한 초기화는 발생하지 않고 -1을 반환한다.
        int result = input2.read(temp2, 2, temp2.length - 2);

        assertEquals(inSrcString, Arrays.toString(temp2));
        assertEquals(result, -1);
    }

    @Test
    @DisplayName("read(byte[] b, int off, int len)에서 off이나 len이 범위를 벗어나는 경우")
    void read2ExceptionTest() {
        byte[] temp = new byte[10];

        ByteArrayInputStream input = new ByteArrayInputStream(inSrc);

        assertThrows(IndexOutOfBoundsException.class,
                () -> input.read(temp, -1, temp.length));
        assertThrows(IndexOutOfBoundsException.class,
                () -> input.read(temp, temp.length + 1, temp.length));
        assertThrows(IndexOutOfBoundsException.class,
                () -> input.read(temp, 0, temp.length + 1));
        assertThrows(IndexOutOfBoundsException.class,
                () -> input.read(temp, 0, -1));
    }

    @Test
    @DisplayName("readAllBytes() 테스트")
    void readAllBytesTest() {
        ByteArrayInputStream input = new ByteArrayInputStream(inSrc);

        assertEquals(inSrcString, Arrays.toString(input.readAllBytes()));
    }

    @Test
    @DisplayName("readNBytes(byte[] b, int off, int len) 테스트")
    void readNBytesTest() {
        byte[] temp = new byte[4];

        ByteArrayInputStream input = new ByteArrayInputStream(inSrc);

        input.readNBytes(temp, 0, temp.length);

        assertEquals(Arrays.toString(new byte[]{2, 3, 4, 5}), Arrays.toString(temp));
    }

    @Test
    @DisplayName("readNBytes(int len) 테스트")
    void readNBytes_InputStream_Test() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(inSrc);

        assertEquals(Arrays.toString(new byte[]{2, 3, 4, 5}),
                Arrays.toString(input.readNBytes(4)));
    }

    /**
     * markSupported()는 mark()와 reset() 지원 여부를 확인한다.
     */
    @Test
    @DisplayName("markSupported() 테스트")
    void markSupportedTest() {
        ByteArrayInputStream input = new ByteArrayInputStream(inSrc);

        assertEquals(input.markSupported(), true);
    }

    @Test
    @DisplayName("available() 테스트")
    void availableTest() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2});

        assertEquals(input.available(), 2);
        input.read();
        assertEquals(input.available(), 1);
        input.read();
        assertEquals(input.available(), 0);
    }

    @Test
    @DisplayName("skip() 테스트")
    void skipTest() {
        ByteArrayInputStream input = new ByteArrayInputStream(inSrc);

        input.skip(1L);
        assertEquals(input.read(), 3);
        input.skip(2L);
        assertEquals(input.read(), 6);
    }

    /**
     * ByteArrayInputStream의 mark(int readlimit)의 readlimit은 사용하지 않는다.
     * 실제 사용에선 markSupported()가 선행되어야 한다.
     */
    @Test
    @DisplayName("mark(int readlimit), reset() 테스트")
    void markAndResetTest() {
        byte[] temp = new byte[10];

        ByteArrayInputStream input = new ByteArrayInputStream(inSrc);

        input.mark(0);
        input.read(temp, 0, temp.length);
        assertEquals(input.available(), 0);
        input.reset();
        assertEquals(input.available(), 10);
    }

    @Test
    @DisplayName("transferTo(OutputStream out) 테스트")
    void transferToTest() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(inSrc);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        input.transferTo(output);

        assertEquals(inSrcString, Arrays.toString(output.toByteArray()));
    }

    /**
     * ByteArrayInputStream의 경우 사용하는 자원이 메모리 밖에 없어 가비지컬렉터에 의해 자동적으로
     * 자원을 반환하므로 close()를 이용하지 않아도 된다.
     */
    @Test
    @DisplayName("close() 테스트")
    void closeTest() {
    }
}
