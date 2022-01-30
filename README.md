# FreeWays


---  

Design Document  
Vakho Akobia, Brennan Hand, Jacob Macleod  

## Introduction  

Freeways is an incident tracking app which lets you track recent incidents in the nearby area, so that you can plan your route safely. It also lets you report your own incidents and help others stay safe as well.  

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

**Given** Incident API Endpoint is available, and there are recent incidents nearby  

**When** I open the app  

**Then** I should see the pins on the map, each denoting a separate incident, with the following attributes:  

Location - Longitude and Latitude determine where the pin will show up on the map.  

Severity - How severe the accident was is based on how many people were injured or deceased. The color of the pin will vary based on the incident severity.  

Details - Short Description of the incident, which will be shown in a popup after clicking a specific pin.  


1.2  

**Given** Incident API Endpoint is available, and there no incidents nearby  

**When** I open the app  

**Then** I should see no pins on the app, and a message/label saying that there are no incidents nearby, and that I can report an incident if I witness/am a part of one.  


### Requirement 2: Creating a report  

#### Scenario:  

The authorized user will create a report and provide details like location, description of the incident, and severity.  

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

**When:** I do not provide enough data or any data into the form  

**Then:** I should receive an error notification to please fill out the data correctly  


## Class Diagram  
![Class Diagram](https://user-images.githubusercontent.com/94126853/151708424-28533245-f91e-498e-a2d3-9ce322394e6f.png)  
 
## Class Diagram Description  
Vakho - Provide Description here  
 
## Scrum Roles, and who will fill those roles  
•	Devops/Product Owner/Scrum Master: Vakho Akobia  
•	Frontend Developer: Jacob Macleod  
•	Integration Developer: Brennan Hand  
 
## Teams meeting  
Weekly Teams Meeting: Sunday 11:00 am

