package com.example.com.wisdomcommunity.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class VerifyUtils {

    /**
     * 判断姓名格式
     * 真实姓名可以是汉字,也可以是字母,但是不能两者都有,也不能包含任何符号和数字
     * 1.如果是英文名,可以允许英文名字中出现空格
     * 2.英文名的空格可以是多个,但是不能连续出现多个
     * 3.汉字不能出现空格
     */
    public static boolean matcherRealName(String value) {
        String regex = "^([\\u4e00-\\u9fa5]+|([a-zA-Z]+\\s?)+)$";
        return matcherRegex(regex, value);
    }

    /**
     * 判断手机号格式 (匹配11数字,并且13-19开头)
     */
    public static boolean matcherPhoneNum(String value) {
        String regex = "^(\\+?\\d{2}-?)?(1[0-9])\\d{9}$";
        return matcherRegex(regex, value);
    }

    /**
     * 判断账号格式 (4-20位字符)
     */
    public static boolean matcherAccount(String value) {
        String regex = "[\\u4e00-\\u9fa5a-zA-Z0-9\\-]{4,20}";
        return matcherRegex(regex, value);
    }

    /**
     * 判断密码格式 (6-位字母或数字,必须同时包含字母和数字)
     * 判断密码格式 (6-位字母或数字)
     */
    public static boolean matcherPassword(String value, int minLen, int maxLen, boolean shouldTwoCategory) {
        String regex = shouldTwoCategory ? String.format(Locale.getDefault(), "(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{%1$d,%2$d}", minLen, maxLen) : String.format(Locale.getDefault(), "^[a-zA-Z0-9]{%1$d,%2$d}$", minLen, maxLen);
        return matcherRegex(regex, value);
    }

    /**
     * 判断邮箱格式
     */
    public static boolean matcherEmail(String value) {
//      String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)" +
//                "+[a-zA-Z]{2,}$";
        String regex = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
        return matcherRegex(regex, value);
    }

    /**
     * 判断IP地址
     */
    public static boolean matcherIP(String value) {
        String regex = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\" +
                "d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\" +
                "d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
        return matcherRegex(regex, value.toLowerCase());
    }

    /**
     * 判断URL (http,https,ftp)
     */
    public static boolean matcherUrl(String value) {
        //String regex = "^(([hH][tT]{2}[pP][sS]?)|([fF][tT][pP]))\\:\\/\\/[wW]{3}\\.[\\w-]+\\.\\w{2,4}(\\/.*)?$";
        String regex = "^(([hH][tT]{2}[pP][sS]?)|([fF][tT][pP]))\\:\\/\\/[\\w-]+\\.\\w{2,4}(\\/.*)?$";
        return matcherRegex(regex, value.toLowerCase());
    }

    /**
     * 判断中国民用车辆号牌
     */
    public static boolean matcherVehicleNumber(String value) {
        String regex = "^[京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼川贵云藏陕甘青宁新渝]?[A-Z][A-HJ-NP-Z0-9学挂港澳练]{5}$";
        return matcherRegex(regex, value.toLowerCase());
    }

    /**
     * 判断身份证号码格式
     */
    public static boolean matcherIdentityCard(String value) {
        IDCard card = new IDCard();
        return card.matcher(value);
    }

    private static class IDCard {
        final int[] WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

        final char[] VALID = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        public boolean isNewCNIDCard(String numbers) {
            numbers = numbers.toUpperCase();
            int sum = 0;
            for (int i = 0; i < WEIGHT.length; i++) {
                final int cell = Character.getNumericValue(numbers.charAt(i));
                sum += WEIGHT[i] * cell;
            }
            int index = sum % 11;
            return VALID[index] == numbers.charAt(17);
        }

        public boolean isOldCNIDCard(String numbers) {
            String yymmdd = numbers.substring(6, 11);
            boolean aPass = numbers.equals(String.valueOf(Long.parseLong(numbers)));
            boolean yPass = true;
            try {
                new SimpleDateFormat("yyMMdd").parse(yymmdd);
            } catch (Exception e) {
                e.printStackTrace();
                yPass = false;
            }
            return aPass && yPass;
        }

        public boolean matcher(String content) {
            if (TextUtils.isEmpty(content)) {
                return false;
            }
            final int length = content.length();
            if (15 == length) {
                try {
                    return isOldCNIDCard(content);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                }
            } else if (18 == length) {
                return isNewCNIDCard(content);
            } else {
                return false;
            }
        }
    }

    /**
     * 是否数值型
     */
    public static boolean isNumeric(String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        char[] chars = input.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
        if (sz > start + 1) {
            if (chars[start] == '0' && chars[start + 1] == 'x') {
                int i = start + 2;
                if (i == sz) {
                    return false;
                }
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9')
                            && (chars[i] < 'a' || chars[i] > 'f')
                            && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--;
        int i = start;
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                if (hasExp) {
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false;
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                return false;
            }
            if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l' || chars[i] == 'L') {
                return foundDigit && !hasExp;
            }
            return false;
        }
        return !allowSigns && foundDigit;
    }

    public static boolean matcherRegex(String regex, String inputValue) {
        return Pattern.compile(regex).matcher(inputValue).matches();
    }

    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }
}