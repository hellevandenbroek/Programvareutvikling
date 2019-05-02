SELECT Bloodsugar.DataproviderID, Bloodsugar.Timestamp, Bloodsugar.Level,
DataProvider.Firstname, DataProvider.Lastname FROM Bloodsugar
INNER JOIN DataProvider ON Bloodsugar.DataproviderID = DataProvider.DataproviderID
WHERE Bloodsugar.DataproviderID = ?;
