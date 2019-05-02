SELECT Pulse.DataproviderID, Pulse.Timestamp, Pulse.BPM,
DataProvider.Firstname, DataProvider.Lastname FROM Pulse
INNER JOIN DataProvider ON Pulse.DataproviderID = DataProvider.DataproviderID
WHERE Pulse.DataproviderID = ?;
