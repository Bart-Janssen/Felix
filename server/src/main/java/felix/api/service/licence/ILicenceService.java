package felix.api.service.licence;

import felix.api.models.Licence;

import java.security.GeneralSecurityException;

public interface ILicenceService
{
    boolean check(Licence licence) throws GeneralSecurityException;
    Licence generate() throws GeneralSecurityException;
    Boolean activate(Licence decryptRsaLicence) throws GeneralSecurityException;
}