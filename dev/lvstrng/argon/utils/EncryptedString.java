package dev.lvstrng.argon.utils;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import org.jetbrains.annotations.NotNull;

public class EncryptedString
implements CharSequence {
    char[] key;
    char[] value;

    public EncryptedString(String s) {
        int len = s.length();
        char[] real = new char[Math.min(len, 64)];
        ThreadLocalRandom current = ThreadLocalRandom.current();
        for (int i = 0; i < real.length; ++i) {
            real[i] = (char)current.nextInt(65535);
        }
        char[] ca = s.toCharArray();
        EncryptedString.doIter(ca, real, 0, ca.length);
        this.key = real;
        this.value = ca;
    }

    public static EncryptedString of(String s) {
        return new EncryptedString(s);
    }

    public static EncryptedString of(String encrypted, String key) {
        return new EncryptedString(encrypted.toCharArray(), key.toCharArray());
    }

    private static void doIter(char[] c, char[] v, int offset, int length) {
        for (int i = offset; i < offset + length; ++i) {
            c[i] = (char)(c[i] ^ v[i % v.length]);
        }
    }

    public EncryptedString(char[] value, char[] key) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int length() {
        return this.value.length;
    }

    @Override
    public char charAt(int index) {
        return (char)(this.value[index] ^ this.key[index % this.key.length]);
    }

    @Override
    @NotNull
    public String toString() {
        char[] copied = Arrays.copyOf(this.value, this.value.length);
        EncryptedString.doIter(copied, this.key, 0, copied.length);
        return new String(copied).intern();
    }

    @Override
    @NotNull
    public CharSequence subSequence(int start, int end) {
        int length = end - start;
        char[] newValue = new char[length];
        char[] newKey = new char[length];
        System.arraycopy(this.value, start, newValue, 0, length);
        for (int i = 0; i < length; ++i) {
            newKey[i] = this.key[(start + i) % this.key.length];
        }
        return new EncryptedString(newValue, newKey);
    }
}
