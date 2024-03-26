package com.networkKnights.ecms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.time.Year;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ResponseEntity<?> getDashboardDetails() {
        //Stored Procedure
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("getDashboardDetails");
        storedProcedure.registerStoredProcedureParameter("paramTotalDepartments", Integer.class, ParameterMode.OUT);
        storedProcedure.registerStoredProcedureParameter("paramTotalEmployees", Integer.class, ParameterMode.OUT);
        storedProcedure.registerStoredProcedureParameter("paramTotalPayout", String.class, ParameterMode.OUT);
        storedProcedure.registerStoredProcedureParameter("paramTotalTeams", Integer.class, ParameterMode.OUT);
        storedProcedure.registerStoredProcedureParameter("paramTotalMale", Integer.class, ParameterMode.OUT);
        storedProcedure.registerStoredProcedureParameter("paramTotalFemale", Integer.class, ParameterMode.OUT);
        storedProcedure.registerStoredProcedureParameter("paramTotalOther", Integer.class, ParameterMode.OUT);
        storedProcedure.registerStoredProcedureParameter("paramTotalPayoutOfCurrentYear", String.class, ParameterMode.OUT);

        storedProcedure.execute();

// Get results by executing SP
        int totalDepartments = (int) storedProcedure.getOutputParameterValue("paramTotalDepartments");
        int totalEmployees = (int) storedProcedure.getOutputParameterValue("paramTotalEmployees");
        String totalPayout = (String) storedProcedure.getOutputParameterValue("paramTotalPayout");
        int totalTeams = (int) storedProcedure.getOutputParameterValue("paramTotalTeams");
        int totalMale = (int) storedProcedure.getOutputParameterValue("paramTotalMale");
        int totalFemale = (int) storedProcedure.getOutputParameterValue("paramTotalFemale");
        int totalOther = (int) storedProcedure.getOutputParameterValue("paramTotalOther");
        String totalPayoutOfCurrentYear = (String) storedProcedure.getOutputParameterValue("paramTotalPayoutOfCurrentYear");


// Get unique payouts of department
        String allPayouts[] = totalPayout.split("###");
        String currentYearPayouts[] = totalPayoutOfCurrentYear.split("###");

        HashMap<String, Object> dashboardDetails = new HashMap<>();

// Set of years
        Set<Integer> years = (Set<Integer>) Arrays.stream(allPayouts).map(value -> value.split(":")).filter(yearWisePayout -> yearWisePayout.length == 2).map(yearWisePayout -> Integer.parseInt(yearWisePayout[0])).collect(Collectors.toSet());
        HashMap<Integer, Integer> yearWiseTotalPayout = new HashMap<>();

// Populate yearWiseTotalPayout as year -> year wise payout
        for (Integer year : years) {
            int sumOfPayouts = Arrays.stream(allPayouts).map(value -> value.split(":")).filter(yearWisePayout -> yearWisePayout.length == 2).filter(yearWisePayout -> yearWisePayout[0].equals(year.toString())).mapToInt(yearWisePayout -> Integer.parseInt(yearWisePayout[1])).sum();
            yearWiseTotalPayout.put(year, sumOfPayouts);
        }

// Combined payout of every department for current year
        int totalPayoutSum = Arrays.stream(currentYearPayouts).map(value -> value.split(":")).filter(yearWisePayout -> yearWisePayout.length == 2).filter(yearWisePayout -> yearWisePayout[0].equals(Year.now().toString())).mapToInt(yearWisePayout -> Integer.parseInt(yearWisePayout[1])).sum();

// Populate dashboardDetails
        dashboardDetails.put("totalDepartments", totalDepartments);
        dashboardDetails.put("totalEmployees", totalEmployees);
        dashboardDetails.put("totalPayout", totalPayoutSum);
        dashboardDetails.put("totalTeams", totalTeams);
        dashboardDetails.put("totalMale", totalMale);
        dashboardDetails.put("totalFemale", totalFemale);
        dashboardDetails.put("totalOther", totalOther);
        dashboardDetails.put("yearWiseTotalPayout", yearWiseTotalPayout);

        return new ResponseEntity<>(dashboardDetails, HttpStatus.OK);
    }
}
