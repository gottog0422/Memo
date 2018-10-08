package com.example.sh.memo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemoData {
    private Integer id;
    private String title;
    private String content;
    private Integer y;
    private Integer m;
    private Integer d;
    private Integer th;
    private Integer tm;
    private Integer ap;
    private Integer star;
}
