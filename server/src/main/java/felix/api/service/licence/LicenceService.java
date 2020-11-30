package felix.api.service.licence;

import felix.api.configuration.LicenceManager;
import felix.api.models.Licence;
import felix.api.repository.LicenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.security.GeneralSecurityException;

@Service
@RequiredArgsConstructor
public class LicenceService implements ILicenceService
{
    private final LicenceRepository licenceRepository;

    @Override
    public boolean check(Licence licence) throws GeneralSecurityException
    {
        Licence dbLicence = this.licenceRepository.findById(licence.getToken()).orElseThrow(EntityNotFoundException::new);
        if (!new LicenceManager().verify(licence)) return false;
        boolean containAllMacs = false;
        for (String mac : licence.getMacs())
        {
            if (dbLicence.getMacs().stream().anyMatch(x -> x.equals(mac)))
            {
                containAllMacs = true;
                break;
            }
            containAllMacs = false;
        }
        if (!containAllMacs) return false;
        return dbLicence.isActivated();
    }

    @Override
    public Licence generate() throws GeneralSecurityException
    {
        Licence licence = this.licenceRepository.save(Licence.builder().activated(false).build());
        licence.setSign(new LicenceManager().sign(licence));
        System.out.println(licence.getSign());
        return licence;
    }

    @Override
    public Boolean activate(Licence decryptRsaLicence) throws GeneralSecurityException
    {
        if (!new LicenceManager().verify(decryptRsaLicence)) return false;
        Licence licence = this.licenceRepository.findById(decryptRsaLicence.getToken()).orElseThrow(EntityNotFoundException::new);
        if (licence.isActivated()) return false;
        licence.setActivated(true);
        licence.setMacs(decryptRsaLicence.getMacs());
        this.licenceRepository.save(licence);
        return true;
    }
}