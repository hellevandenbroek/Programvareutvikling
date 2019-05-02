-- TDT4140 g11 database schema

--
-- Table structure for table `DataProvider`
--

CREATE TABLE `DataProvider` (
  `DataproviderID` int(255) NOT NULL AUTO_INCREMENT COMMENT 'Primary',
  `SSN` varchar(20) NOT NULL,
  `FirstName` varchar(255) NOT NULL,
  `LastName` varchar(255) NOT NULL,
  `BirthDate` date NOT NULL,
  `Sex` enum('M', 'F', 'O') DEFAULT NULL,
  `Weight` decimal(10,0) NOT NULL COMMENT 'kg',
  `Height` decimal(10,0) NOT NULL COMMENT 'cm',
  `Status` enum('ACTIVE','INACTIVE','DEACTIVATED') DEFAULT NULL,
  `StartDate` date NOT NULL,
  `Email` varchar(256) NOT NULL,
  `PhoneNum` varchar(11) NOT NULL,
  CONSTRAINT DataProvider_pk PRIMARY KEY (DataproviderID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `Bloodsugar`
--

CREATE TABLE `Bloodsugar` (
  `DataproviderID` int(255) NOT NULL,
  `Timestamp` datetime NOT NULL,
  `Level` double NOT NULL,
  CONSTRAINT Bloodsugar_pk PRIMARY KEY (DataproviderID, Timestamp),
  CONSTRAINT Bloodsugar_fk FOREIGN KEY (DataproviderID)
    REFERENCES DataProvider(DataproviderID)
    ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `Pulse`
--

CREATE TABLE `Pulse` (
  `DataproviderID` int(255) NOT NULL,
  `Timestamp` datetime NOT NULL,
  `BPM` int(11) DEFAULT NULL,
  CONSTRAINT Pulse_pk PRIMARY KEY (DataproviderID, Timestamp),
  CONSTRAINT Pulse_fk FOREIGN KEY (DataproviderID)
    REFERENCES DataProvider(DataproviderID)
    ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `ServiceProvider`
--

CREATE TABLE `ServiceProvider` (
  `Username` varchar(256) NOT NULL,
  `PasswordHash` varchar(256) NOT NULL,
  `Email` varchar(256) NOT NULL,
  CONSTRAINT ServiceProvider_pk PRIMARY KEY (Username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `DeletedData`
--

CREATE TABLE `DeletedData` (
   `Timestamp` datetime NOT NULL,
   `DataProviderID` int(255) NOT NULL,
   CONSTRAINT DeletedData_pk PRIMARY KEY (DataProviderID),
   CONSTRAINT DeletedData_fk FOREIGN KEY (DataProviderID)
    REFERENCES DataProvider(DataproviderID)
    ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

