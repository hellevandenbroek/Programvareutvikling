Delete b FROM Bloodsugar b
INNER JOIN DeletedData d
ON b.DataproviderID = d.DataProviderID
WHERE b.Timestamp < d.Timestamp
AND d.DataProviderID = ?;