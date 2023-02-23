package com.am.assignment1.main;

import com.am.assignment1.dto.AccountDTO;
import com.am.assignment1.service.AccountExcelFileService;
import com.am.assignment1.service.AccountFileService;
import com.am.assignment1.service.CustomerNameGeneratorService;

import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
//@Slf4j
public class GenerateAccountFile {
    private static final Logger log = LoggerFactory.getLogger(GenerateAccountFile.class);

    @Autowired
    private AccountFileService accountfileservice;
    @Autowired
    private AccountExcelFileService accountexcelfileservice;
    @Autowired
    private CustomerNameGeneratorService customerNameGeneratorService;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static List<String> acclist;
    public static Random rd = new Random();

    public static float leftLimit = 1000F;
    public static float rightLimit = 2000F;


    @PostConstruct
    public void createAccountFile() throws IOException {
        List<AccountDTO> accounts = new ArrayList<>();
//        List<String> names = new ArrayList<>();
        List<String> names=customerNameGeneratorService.getNames();
        accountfileservice.generateFile();

        for (int i = 0; i < 20; i++){

            String generateName = customerNameGeneratorService.generateName();
            names.add(generateName);
            log.info(names.toString());
            // account id
            String accountId = String.format("%012d", i);
            // account balance
            float input = rd.nextFloat();
            float balance = leftLimit + Float.parseFloat(df.format(input)) * (rightLimit - leftLimit);
            // create date
            LocalDate dateNow = LocalDate.now();
            AccountDTO accountDTO = new AccountDTO(accountId,names.get(i), balance, dateNow);
//            log.info(names.get(i).toString());
            accounts.add(accountDTO);

        }


        accountfileservice.writeToFile(accounts);
        accountexcelfileservice.writeExcel(accounts);

    }

    @PostConstruct
    public void createAccountExcelFile() throws IOException {
        List<AccountDTO> accounts = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> listOfNames=customerNameGeneratorService.getNames();
        for(String name:listOfNames){
            names.add(name);
        }

        for (int i = 1; i < 20; i++) {
            // name
            String generateName = customerNameGeneratorService.generateName();
            names.add(generateName);
            // account id
            String accountId = String.format("%012d", i);
            // account balance
            float input = rd.nextFloat();
            float balance = Float.parseFloat(df.format(input));
            // create date
            LocalDate dateNow = LocalDate.now();
            AccountDTO accountDTO = new AccountDTO(accountId,names.get(i), balance, dateNow);
            accounts.add(accountDTO);

        }

        accountexcelfileservice.writeExcel(accounts);

    }


}
