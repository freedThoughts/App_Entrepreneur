package com.mitti.entrepreneur.repository;

import com.mitti.entrepreneur.model.ProfitMakingCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfitMakingCompanyRepository extends JpaRepository<ProfitMakingCompany, Integer> {

    ProfitMakingCompany findByOwnerName(String ownerName);
}
