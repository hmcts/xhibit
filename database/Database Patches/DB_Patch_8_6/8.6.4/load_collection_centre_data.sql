-- Wales
insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('HMCTS Court Code 3357','Port Talbot Justice Centre', 'Harbourside Road', null, 'Port Talbot', null, 'SA13 1SB', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Wales', 'Wales', 'Wales Collection Centre', 'WalesEnf.TFO@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='HMCTS Court Code 3357'));


-- North West
insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Lancashire Fines Unit','PO Box 64', 'Colne Road', 'Reedley', 'Burnley', null, 'BB10 2NQ', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Lancashire', 'Lancashire', 'Lancashire Fines Unit', 'LN-ReedleyMcAcc@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Lancashire Fines Unit'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Cumbria Central Payments Unit','TheCourthouse', 'Burneside Road', null, 'Kendal', 'Cumbria', 'LA9 4TJ', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Cumbria', 'Cumbria', 'Cumbria Central Payments Unit', 'CM-KendalMcEng@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Cumbria Central Payments Unit'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Cheshire Cen Pay Enforcement','PO Box 101', 'Halton Lea', null, 'Runcorn', 'Cheshire', 'WA7 2GE', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Cheshire', 'Cheshire', 'Cheshire Central Payments and Enforcement Centre', 'CH-ME-Supported-compliance@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Cheshire Cen Pay Enforcement'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Merseyside Cen Pay Enforcement','PO Box 104', 'Halton Lea', null, 'Runcorn', 'Cheshire', 'WA7 2GE', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Merseyside', 'Merseyside', 'Merseyside Central Payments and Enforcement Centre', 'CH-ME-Supported-compliance@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Merseyside Cen Pay Enforcement'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Grt Manc Accts Enforcement','PO Box 4372', 'Manchester City Mags Court', 'Crown Square', 'Manchester', null, 'M61 0EQ', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Greater Manchester', 'Greater Manchester', 'Greater Manchester Accounts Enforcement Centre', 'GM-AEU@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Grt Manc Accts Enforcement'));


-- London
insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('London Coll Compliance','Dept 2646', 'PO Box 31092', null, 'London', null, 'SW19 3WS', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('London', 'London', 'London Collection and Compliance Centre', 'lccccomplianceunit@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='London Coll Compliance'));


-- South East
insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Area Enforcement (Bedford 10)','3 St Pauls Square', null, null, 'Bedford', null, 'MK40 1SQ', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Bedfordshire and Hertfordshire', 'Bedfordshire and Hertfordshire', 'Area Enforcement Unit (Bedford 10)', 'BD-Enforcement@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Area Enforcement (Bedford 10)'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Thames Valley Enforcement','Business Centre', 'PO Box 267', null, 'Bicester', 'Oxfordshire', 'OX25 4ZD', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Oxfordshire and Berkshire', 'Oxfordshire and Berkshire', 'Thames Valley Enforcement', 'no-email-yet@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Thames Valley Enforcement'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Brighton Magistrates Court','Edward Street', 'DX153460', null, 'Brighton', 'East Sussex', 'BN2 0LG', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Kent, Surrey, Sussex or Bucks', 'Kent, Surrey, Sussex or Bucks', 'Kent, Surrey, Sussex or Bucks', 'ss-eastenfmcenq@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Brighton Magistrates Court' and address_3='DX153460'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('North East Suffolk MC','Old Nelson Street', 'DX41219', null, 'Lowestoft', null, 'NR32 1HJ', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Cambs, Norfolk, Suffolk or Essex', 'Cambs, Norfolk, Suffolk or Essex', 'Cambs, Norfolk, Suffolk or Essex', 'suffolkenfwork@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='North East Suffolk MC' and address_3='DX41219'));


-- South West
insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Avon and Somerset Finance','PO Box 480', 'DX152360', null, 'Weston Super Mare', null, 'BS23 9BE', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Avon and Somerset', 'Avon and Somerset', 'Avon and Somerset', 'DO-PooleEnforcement@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Avon and Somerset Finance' and address_3='DX152360'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Central Finance Unit','First Floor', 'Plymouth Magistrates Court', 'St Andrew Street', 'Plymouth', null, 'PL1 2DP', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Devon and Cornwall', 'Devon and Cornwall', 'Devon and Cornwall', 'cfuwarrants@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Central Finance Unit' and postcode='PL1 2DP'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('HMCTS Gloucestershire','PO Box 9051', null, null, 'Gloucester', null, 'GL1 2XG', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Gloucestershire', 'Gloucestershire', 'Gloucestershire', 'gs-mcaccounts@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='HMCTS Gloucestershire' and postcode='GL1 2XG'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Money Handling Office Swindon','HMCTS', 'PO Box 3674', null, 'Swindon', null, 'SN3 9BU', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Dorset, Hamps, IOW and Wilts', 'Dorset, Hamps, IOW and Wilts', 'Dorset, Hamps, IOW and Wilts', 'WI-WILTSMCACCOUNTS@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Money Handling Office Swindon' and postcode='SN3 9BU'));


-- Midlands
insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('West Midlands Central Finance','Enforcement Team', 'Victoria Law Courts', 'Corporation Street', 'Birmingham', null, 'B4 6QA', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('West Midlands', 'West Midlands', 'West Midlands', 'wm-enforcementteam@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='West Midlands Central Finance' and postcode='B4 6QA'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Loughborough Magistrates Court','60 Pinfold Gate', null, null, 'Lougborough', 'Leicestershire', 'LE11 1AZ', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Leicestershire', 'Leicestershire', 'Leicestershire', 'LE-LoughMCFines@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Loughborough Magistrates Court' and postcode='LE11 1AZ'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Nottingham and Newark Mags Ct','Carrington Street', null, null, 'Nottingham', null, 'NG2 1EE', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Nottinghamshire', 'Nottinghamshire', 'Nottinghamshire', 'Nt-NottmWarrants@hmcourts-service.gsi.gov.uk', (select address_id from xhb_address where address_1='Nottingham and Newark Mags Ct' and postcode='NG2 1EE'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Central Finance Unit','The Court House', 'Bryans Lane', 'Rugeley', 'Rugeley', null, 'WS15 9EG', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('West Mercia', 'West Mercia', 'West Mercia', 'HW-WarrantOffice@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Central Finance Unit' and postcode='WS15 9EG'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Ctrl Finance and Enforcement','The Court House', 'Bryans Lane', 'Rugeley', 'Rugeley', null, 'WS15 2FS', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Staffordshire', 'Staffordshire', 'Staffordshire', 'St-Warrants@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Ctrl Finance and Enforcement' and postcode='WS15 2FS'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Regents Pavilion','Summerhouse Road', 'Moulton Park', null, 'Northampton', null, 'NN3 6AS', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Northamptonshire', 'Northamptonshire', 'Northamptonshire', 'northantsenquiries@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Regents Pavilion' and postcode='NN3 6AS'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Lincolnshire Central Finance','358 High Street', null, null, 'Lincoln', null, 'LN5 7QA', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Lincolnshire', 'Lincolnshire', 'Lincolnshire', 'LI-lincolnmccfu@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Lincolnshire Central Finance' and postcode='LN5 7QA'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('The Court House','St Marys Gate', null, null, 'Derby', 'Derbyshire', 'DE1 3JR', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Derbyshire', 'Derbyshire', 'Derbyshire', 'DB-distresswarrants@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='The Court House' and postcode='DE1 3JR'));


-- North East
insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('HMCTS Cleveland Enforcement','Teesside Law Courts', 'Victoria Square', null, 'Middlesborough', null, 'TS1 2AS', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Cleveland', 'Cleveland', 'Cleveland', 'CL-MbroMCCFines@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='HMCTS Cleveland Enforcement' and postcode='TS1 2AS'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Darlington Fines Enforcement','Magistrates Court', 'PO Box 107', null, 'Darlington', null, 'DL1 1ZD', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Durham', 'Durham', 'Durham', 'du-durhammcfinance@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Darlington Fines Enforcement' and postcode='DL1 1ZD'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('Northumbria Ctrl Enforcement','PO Box 826', null, null, 'North Shields', null, 'NE29 1DZ', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('Northumbria', 'Northumbria', 'Northumbria', 'no-ceuadmin@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='Northumbria Ctrl Enforcement' and postcode='NE29 1DZ'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('South Yorkshire Enforcement','Castle Street', null, null, 'Sheffield', null, 'S3 8LU', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('South Yorkshire and Humber', 'South Yorkshire and Humber', 'South Yorkshire and Humber', 'sheffieldcompliance@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='South Yorkshire Enforcement' and postcode='S3 8LU'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('North Yorkshire Ctrl Finance','PO Box 87', null, null, 'Northallerton', null, 'DL7 8GF', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('North Yorkshire', 'North Yorkshire', 'North Yorkshire', 'no-email-yet@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='North Yorkshire Ctrl Finance' and postcode='DL7 8GF'));

insert into xhb_address (address_1, address_2, address_3, address_4, town, county, postcode, country) values ('West Yorks Coll Enforcement','PO Box 135', null, null, 'Leeds', null, 'LS27 7ZT', null);
insert into xhb_collection_centre (display_name, full_name, description, email_address, address_id) values ('West Yorkshire', 'West Yorkshire', 'West Yorkshire', 'wy-cecmcenf@hmcts.gsi.gov.uk', (select address_id from xhb_address where address_1='West Yorks Coll Enforcement' and postcode='LS27 7ZT'));



commit;