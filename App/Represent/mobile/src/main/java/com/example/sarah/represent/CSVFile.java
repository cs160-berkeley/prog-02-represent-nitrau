package com.example.sarah.represent;

/**
 * Created by Sarah on 3/13/2016.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CSVFile {
    InputStream inputStream;
    private HashSet<String> zips;

    public CSVFile(InputStream inputStream) {
        this.inputStream = inputStream;
        zips = new HashSet<>();
    }

    public void read() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                zips.add(row[0]);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
    }

    public boolean find(String s) {
        return zips.contains(s);
    }
}
