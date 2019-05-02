SELECT p.DataproviderID, p.Timestamp, p.BPM,

d.Firstname, d.Lastname FROM Pulse p

INNER JOIN DataProvider d ON p.DataproviderID = d.DataproviderID

WHERE p.DataproviderID NOT IN (

  SELECT dd.DataProviderID
  FROM DeletedData dd
  WHERE p.Timestamp < dd.Timestamp

);

