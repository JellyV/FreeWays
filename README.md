# FreeWays


---  

Design Document  
Vakho Akobia, Brennan Hand, Jacob Macleod  

## Introduction  

Freeways is an incident tracking app which lets you track recent incidents in the nearby area, so that you can plan your route safely. It lets you report your own incidents and help others stay safe.  

## Storyboard  
![App Storyboard](https://user-images.githubusercontent.com/94126853/151708250-31d2bf23-7c66-4fa0-9aa2-68472510323b.png)  

## Functional Requirements  

### Requirement 1: Mapping incidents nearby  

#### Scenario  

As a User, I want to see a map of incidents nearby and view details of each incident, such as the severity of the incident and the short description.  

#### Dependencies  

The incident API endpoint is available and accessible.  

The device has GPS capabilities, and the user has been granted location access.  

#### Assumptions  

At least a few incidents have occurred in a nearby area recently.  

#### Examples  

1.1  

**Given** Incident API Endpoint is available, and the details for the incident are retrieved. 

**When** I open the app, and select the pin.

**Then** I should see the details pulled from the database for the incident and the following attributes:

Location - Longitude and Latitude determine where the pin will show up on the map.  

Severity - How severe the accident was is based on how many people were injured or deceased. The color of the pin will vary based on the incident severity.  

Details - Short Description of the incident, which will be shown in a popup after clicking a specific pin.  


1.2  

**Given** Incident API Endpoint is available, and there no incidents nearby  

**When** I open the app  

**Then** I should see no pins on the app, and a message/label saying that there are no incidents nearby, and that I can report an incident if I witness/am a part of one.  


### Requirement 2: Creating a report  

#### Scenario:  

As an authorized User, I want to create a report and provide details such as: location, description of the incident, and severity. So that we can view the details and submit the report to the app’s database.

#### Dependencies:  

The report form is available and accessible.  

The device has GPS capabilities, and the user has granted location access.  

#### Assumptions:  

Form details are stated in English.  

#### Examples:  

2.1   

**Given** the report form is available and accessible  

**Given** GPS details are available  

**When**  

*	Select create the incident form  
*	Add details:  

    o	Location (user pinned location)  
    o	Description of incident  
    o	The severity of the incident:  

       *	High: Deaths involved  
       *	Medium: Injuries involved  
       *	Low: No injuries  
  
* Then when the officer posts the incident report, I will see the pin updated to the map.  

2.2  

**Given** the report form is available and accessible  

**Given** GPS details are available  

**When:** I do not provide the corresponding details like, location, description of incident, and severity. 

**Then:** I should receive an error notification to please provide the details above correctly.


## Class Diagram  
![class_diagram](https://user-images.githubusercontent.com/17710991/154614171-b69b053c-4af7-4f55-9c05-0686fc6f2e17.png)

 
## Class Diagram Description  
**MainActivity:** The first screen the user sees. The user will be able to view the map, defaulted to his current location, and the pins on that map; Each pin denoting an incident. Upon clicking the pin, the user will see the details of that incident.

**RetrofitClientInstance:** Bootstrap class required for Retrofit.

**Incident:** Noun class that represents a single incident

**State:** Noun class that represents a state

**IIncidentDAO:** Interface for Retrofit to find and parse Incident JSON

**IIncidentService:** Service (business logic class) for Incident
  
## Scrum Roles, and who will fill those roles  
•	Devops/Product Owner/Scrum Master: Vakho Akobia  
•	Frontend Developer: Jacob Macleod  
•	Integration Developer: Brennan Hand  
 
## Teams meeting  
Weekly Teams Meeting: Sunday 11:00 am

