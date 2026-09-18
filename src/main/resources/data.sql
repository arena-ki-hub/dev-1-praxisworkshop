INSERT INTO employee (first_name, last_name, username, weekly_target_hours) VALUES
    ('Anna', 'Schmidt', 'a.schmidt', 40.0),
    ('Max', 'Mueller', 'm.mueller', 40.0),
    ('Julia', 'Klein', 'j.klein', 30.0);

INSERT INTO time_entry (employee_id, date, hours, task_description, absence) VALUES
    (1, DATEADD('DAY', -3, CURRENT_DATE), 8.0, 'Kundenprojekt', false),
    (1, DATEADD('DAY', -2, CURRENT_DATE), 8.0, 'Kundenprojekt', false),
    (1, DATEADD('DAY', -1, CURRENT_DATE), 8.0, 'Urlaub', true);
