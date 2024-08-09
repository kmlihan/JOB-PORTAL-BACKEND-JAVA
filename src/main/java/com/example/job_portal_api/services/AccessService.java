package com.example.job_portal_api.services;

import java.util.UUID;

public interface AccessService {
    boolean isAdmin();

    boolean isCompanyRole();

    boolean isUserRole();

    boolean hasUserAccess(UUID userId);

    boolean hasCompanyAccess(UUID companyId);

    boolean hasJobAccess(UUID jobId);
}
