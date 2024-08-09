package com.example.job_portal_api.utils;

import com.example.job_portal_api.dtos.AttachmentDto;
import com.example.job_portal_api.entities.Attachment;

public class LogoUtil {
    public static String getLogoUrl(Attachment logo) {
            if (logo != null) {
                String base64String = String.valueOf(logo.getData());
                return "data:" + logo.getFileType() + ";base64," + base64String;
            }

        return "";
    }
}
