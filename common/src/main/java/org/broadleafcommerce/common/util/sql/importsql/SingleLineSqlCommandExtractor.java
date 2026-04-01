/*-
 * #%L
 * BroadleafCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2026 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package org.broadleafcommerce.common.util.sql.importsql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Replacement for org.hibernate.tool.hbm2ddl.SingleLineSqlCommandExtractor
 * which was removed in Hibernate 7. This provides basic SQL command extraction
 * functionality for demo/test data import.
 * 
 * @author Broadleaf Commerce (Hibernate 7 migration)
 */
public class SingleLineSqlCommandExtractor {

    public String[] extractCommands(Reader reader) {
        List<String> commands = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            StringBuilder currentCommand = new StringBuilder();
            
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                
                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("--") || line.startsWith("#")) {
                    continue;
                }
                
                currentCommand.append(line);
                
                // Check if line ends with semicolon (end of command)
                if (line.endsWith(";")) {
                    String command = currentCommand.toString();
                    // Remove trailing semicolon
                    if (command.endsWith(";")) {
                        command = command.substring(0, command.length() - 1).trim();
                    }
                    if (!command.isEmpty()) {
                        commands.add(command);
                    }
                    currentCommand = new StringBuilder();
                } else {
                    // Add space between lines for multi-line commands
                    currentCommand.append(" ");
                }
            }
            
            // Add any remaining command
            if (currentCommand.length() > 0) {
                String command = currentCommand.toString().trim();
                if (!command.isEmpty()) {
                    commands.add(command);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading SQL commands", e);
        }
        
        return commands.toArray(new String[0]);
    }
}
