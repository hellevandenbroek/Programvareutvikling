SELECT b.DataproviderID, b.Timestamp, b.Level,

d.Firstname, d.Lastname FROM Bloodsugar b

INNER JOIN DataProvider d ON b.DataproviderID = d.DataproviderID

WHERE b.DataproviderID NOT IN (

  SELECT dd.DataProviderID
  FROM DeletedData dd
  WHERE b.Timestamp < dd.Timestamp

);



