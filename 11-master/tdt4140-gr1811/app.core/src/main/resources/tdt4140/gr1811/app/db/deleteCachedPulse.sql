Delete p FROM Pulse p
INNER JOIN DeletedData d
ON p.DataproviderID = d.DataProviderID
WHERE p.Timestamp < d.Timestamp
AND d.DataProviderID = ?;