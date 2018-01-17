package com.example.com.support_business.module;

/**
 * Created by rhm on 2018/1/16.
 */

public class PageEntity<R> extends ListEntity<R> {
    public Page page;

    public static class Page {
        public static final long PAGE_START = 0;
        /**
         * 每页条数
         */
        public long num;
        /**
         * 起始条数，从零开始
         */
        public long start;
        /**
         * 总条数
         */
        public long total;

        /**
         * 下一页起始条数
         */
        public long nextStart() {
            return num + start;
        }

        public boolean isEnd() {
            return nextStart() >= total;
        }

        public boolean isEmpty() {
            return total == 0;
        }
    }
}
