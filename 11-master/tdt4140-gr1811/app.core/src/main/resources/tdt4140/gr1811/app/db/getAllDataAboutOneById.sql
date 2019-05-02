SELECT
  d.DataproviderID,
  d.FirstName,
  d.LastName,
  d.BirthDate,
  d.Sex,
  d.Height,
  d.Weight,
  d.Status,
  d.StartDate,
  d.Email,
  d.PhoneNum,
  b.Timestamp AS blood_time,
  b.Level,
  p.Timestamp AS pulse_time,
  p.BPM
FROM DataProvider d
LEFT JOIN Pulse p ON d.DataproviderID = p.DataproviderID
LEFT JOIN Bloodsugar b ON d.DataproviderID = b.DataproviderID
WHERE d.DataproviderID = ?;
