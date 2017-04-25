<!DOCTYPE html>
<html>
        <head>
            <title>ZIP Find</title>
            <link rel="stylesheet" href="../css/zipFindStyle.css" type="text/css"/>
            <script src="https://maps.google.com/maps/api/js?sensor=false"></script>
            <script type="text/javascript" src="../js/zipFindScript.js"></script>
        </head>
        
        <body>
            <div id="call">
                <h1>Call</h1>
                <form id="zipForm" method="get" action="<?php echo $_SERVER['PHP_SELF'];?>">
                    <p>Enter a US city (capitalize and spell correctly. Ex: Atlanta) and state (use abbreviation & CAPS only. Ex: GA) to find the location on a map!
                    <br>
                    <input type="text" name="city" id="city">
                    <input type="text" name="state" id="state"> 
                    <br>
                    <input type="submit"></p>
                </form>
            </div>
            
            <hr>
            
            <div id="response">
                <h1>Response</h1>
                <div id="mapholder"></div>
                <div id="tripholder">&nbsp;</div>
                <?php
                    if(!empty($_GET)) {
                        $filename = "../data/zipFile.csv";
                        $datafile = fopen($filename, "r") or die("You don' goofed! Unable to open file.");
                       
                       while(!feof($datafile)){
                           $dataline = fgetcsv($datafile);
                           if($dataline[3] == $_GET['city'] && $dataline[4] == $_GET['state']){
                               echo "<script> showMap(" . $dataline[0] . "," . $dataline[1] . "," . $dataline[2] . ",'" . $dataline[3] . "','" . $dataline[4] . "','" . $dataline[5] . "'); </script>" ;
                               break;
                           }
                           
                       }
                    
                    fclose($datafile);
                    
                    }

                ?> 
            </div>

        </body>
</html>