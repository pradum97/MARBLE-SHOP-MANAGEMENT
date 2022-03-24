# INSTALLATION GUIDE

<div style="text-align: center;"><H3 style="color:red" >#1 - POSTGRESQL</H3></div>

<p><B>STEP 1 : </B> DOWNLOAD POSTGRESQL FROM THE GIVEN LINK :<b> <a href="https://www.postgresql.org/download/">DOWNLOAD POSTGRESQL</a></b> </p>
<p><B>STEP 2 : </B> INSTALL POSTGRESQL</p>

- #### AFTER SUCCESSFULLY INSTALLED POSTGRESQL <br>

<p><B>1.  </B> OPEN `pgAdmin` ( POSTGRESQL )</p>
<p><B>2. </B> CREATE NEW DATABASE</p>

#### Note : Please Remember 
    1. Database Name 
    2. Port Number 
    3. Username
    3. Password

<p><B>2. </B> EXECUTE ALL QUERY SCRIPT IN `Query Tool` <b> <a href="https://github.com/pradum97/MARBLE-SHOP-MANAGEMENT/blob/development/Marble%20Query%20Script.txt">CLICK TO COPY QUERY SCRIPT</a></b></p>


<div style="text-align: center;"><H3 style="color:red" >#2 - CLONE PROJECT FROM GIT</H3></div>

##### COPY THIS CODE AND THEN RUN IT AT THE COMMAND PROMPT
     git clone --branch development https://github.com/pradum97/MARBLE-SHOP-MANAGEMENT.git

#### * AFTER SUCCESSFULLY CLONED AND THEN OPEN PROJECT IN INTELLIJ IDEA 


###### GO TO : `src/main/resources/com/shop/management/util/query.properties` AND THEN OPEN `query.properties`

    DB_USERNAME = < Enter Your Database Username >
    DB_PASSWORD = <Enter Yur Database Password>
    DB_URL= jdbc:postgresql://localhost:<Enter Port Number>/<Enter Database Name>

<img src="https://github.com/pradum97/MARBLE-SHOP-MANAGEMENT/blob/development/ScreenShot/querySc.png" alt="">






