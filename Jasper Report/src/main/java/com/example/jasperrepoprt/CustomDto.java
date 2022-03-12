package com.example.jasperrepoprt;

import java.util.List;

public class CustomDto {

    private String productName;
    private String productType;

    private List<CompanyDto> companyDtos;
    private List<ProgrammingDto> programmingDtos;
    private List<CustomDto> customDtos;

    public CustomDto() {
    }

    public List<CustomDto> getCustomDtos() {
        return customDtos;
    }

    public void setCustomDtos(List<CustomDto> customDtos) {
        this.customDtos = customDtos;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<CompanyDto> getCompanyDtos() {
        return companyDtos;
    }

    public void setCompanyDtos(List<CompanyDto> companyDtos) {
        this.companyDtos = companyDtos;
    }

    public List<ProgrammingDto> getProgrammingDtos() {
        return programmingDtos;
    }

    public void setProgrammingDtos(List<ProgrammingDto> programmingDtos) {
        this.programmingDtos = programmingDtos;
    }
}
