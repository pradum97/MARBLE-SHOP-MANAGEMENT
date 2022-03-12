package com.example.jasperrepoprt;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        List<CustomDto> customDtos = new ArrayList<>();
        customDtos.add(new CustomDto());

        List<CompanyDto> companyDtos = new ArrayList<CompanyDto>();

        CompanyDto companyDto ;

        companyDto = new CompanyDto();
        companyDto.setNo(1);
        companyDto.setName("WIPRO");
        companyDtos.add(companyDto);

        companyDto = new CompanyDto();
        companyDto.setNo(2);
        companyDto.setName("TCS");
        companyDtos.add(companyDto);


        companyDto = new CompanyDto();
        companyDto.setNo(3);
        companyDto.setName("GOOGLE");
        companyDtos.add(companyDto);


        companyDto = new CompanyDto();
        companyDto.setNo(4);
        companyDto.setName("RELIANCE");
        companyDtos.add(companyDto);

        List<ProgrammingDto> programmingDtos = new ArrayList<ProgrammingDto>();

        ProgrammingDto programmingDto;
        programmingDto = new ProgrammingDto();
        programmingDto.setNo(1);
        programmingDto.setName("JAVA");
        programmingDtos.add(programmingDto);

        programmingDto = new ProgrammingDto();
        programmingDto.setNo(2);
        programmingDto.setName("PYTHON");
        programmingDtos.add(programmingDto);

        programmingDto = new ProgrammingDto();
        programmingDto.setNo(3);
        programmingDto.setName("JAVASCRIPT");
        programmingDtos.add(programmingDto);

        programmingDto = new ProgrammingDto();
        programmingDto.setNo(3);
        programmingDto.setName("C++");
        programmingDtos.add(programmingDto);

        customDtos.get(0).setCompanyDtos(companyDtos);
        customDtos.get(0).setProgrammingDtos(programmingDtos);

        List<Pk> p = new ArrayList<>();
        Pk pk ;

        pk = new Pk("Pradum",100);p.add(pk);
        pk = new Pk("sujit",100);p.add(pk);
        pk = new Pk("mith",200);p.add(pk);
        pk = new Pk("dhu",300);p.add(pk);
        pk = new Pk("sanoj",300);p.add(pk);

        JRBeanCollectionDataSource jrbean = new JRBeanCollectionDataSource(p);
        JRBeanCollectionDataSource jrbean3 = new JRBeanCollectionDataSource(p);
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "PradumKumar");
        map.put("ds",jrbean);
        map.put("pk",jrbean3);


        File file = new File("D:\\Desktop Application\\MARBLE-SHOP-MANAGEMENT\\ReportTest\\reportTest.jrxml");

        try {

            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource jrbean2 = new JRBeanCollectionDataSource(customDtos);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, map, jrbean2);
            JasperExportManager.exportReportToPdfFile(print, "D:\\Desktop Application\\MARBLE-SHOP-MANAGEMENT\\iReport\\rr.pdf");

            JasperViewer.viewReport(print);
            System.out.println("successful");

        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
