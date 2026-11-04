package be.dikkenek.colocationbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity(name = "Landlord")
public class LandlordEntity extends UserEntity
{
    @OneToMany
    private List<FlatshareEntity> flatshares;

    public LandlordEntity()
    {
    }
}
