package com.ucan.shiro;

/**
 * @Description: 常见静态资源后缀名
 * @author liming.cen
 * @date 2024-07-13 21:00:01
 * 
 */
public enum ResourceSuffix {

    HTML(".html"), HTM(".htm"), CSS(".css"), JS(".js"), SVG(".svg"), ICO(".ico"), JPG(".jpg"), GIF(".gif"),
    JPEG(".jpeg"), PNG(".png"), JSON(".json"), PDF(".pdf"), TTF(".ttf"), EOT(".eot"), OTF(".otf"), WOFF(".woff"),
    WOFF2(".woff2"), XML(".xml"), CSV(".csv"), TXT(".txt");

    public String value;

    ResourceSuffix(String value) {
        this.value = value;
    }

    /**
     * 返回具体后缀值
     * 
     * @return
     */
    public String getVal() {
        return this.value;
    }
//HTML: .html, .htm
//CSS: .css
//JavaScript: .js
//图片文件: .jpg, .jpeg, .png, .gif, .bmp, .svg, .ico
//字体文件: .ttf, .otf, .woff, .woff2, .eot
//视频文件: .mp4, .webm, .ogg, .mov, .avi, .wmv, .flv, .mkv
//音频文件: .mp3, .wav, .ogg, .aac, .flac
//PDF 文件: .pdf
//XML 文件: .xml
//JSON 文件: .json
//CSV 文件: .csv
//文本文件: .txt, .log, .md
//压缩文件: .zip, .rar, .tar.gz, .7z
//Microsoft Office 文件: .doc, .docx, .xls, .xlsx, .ppt, .pptx
//Adobe 文件: .psd, .ai, .eps, .indd
//文件下载: .exe, .dmg, .pkg, .deb, .rpm
//日历文件: .ics
//数据文件: .csv, .tsv, .xml, .json
//字幕文件: .srt, .sub
//矢量图形文件: .svg, .eps, .ai

}
