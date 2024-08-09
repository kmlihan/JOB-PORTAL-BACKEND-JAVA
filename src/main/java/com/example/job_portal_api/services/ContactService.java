package com.example.job_portal_api.services;

import com.example.job_portal_api.dtos.ContactRequest;

public interface ContactService {
    void sendEmail(ContactRequest contactRequest);
}
