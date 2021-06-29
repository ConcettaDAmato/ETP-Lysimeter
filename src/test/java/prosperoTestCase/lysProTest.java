package prosperoTestCase;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.jgrasstools.gears.io.rasterreader.OmsRasterReader;
import org.jgrasstools.gears.io.shapefile.OmsShapefileFeatureReader;
import org.jgrasstools.gears.io.timedependent.OmsTimeSeriesIteratorReader;
import org.jgrasstools.gears.io.timedependent.OmsTimeSeriesIteratorWriter;
import org.jgrasstools.gears.libs.monitor.PrintStreamProgressMonitor;
import org.junit.*;

//import etpPointCase.OmsTranspiration;
//import prospero.OmsProspero;
import prospero.OmsLysProspero;

/**
 * Test LysProspero-Schymanski & Or evapotranspiration.
 * @author D'Amato Concetta, Michele Bottazzi (concetta.damato@unitn.it)
 */
public class lysProTest{
	@Test
    public void Test() throws Exception {
		String startDate= "2013-12-15 15:00";
        String endDate	= "2013-12-15 20:00";
        int timeStepMinutes = 60;
        String fId = "ID";

        PrintStreamProgressMonitor pm = new PrintStreamProgressMonitor(System.out, System.out);
        
        OmsRasterReader DEMreader = new OmsRasterReader();
		DEMreader.file = "resources/Input/dataET_point/Cavone/1/dem_1.tif";
		DEMreader.fileNovalue = -9999.0;
		DEMreader.geodataNovalue = Double.NaN;
		DEMreader.process();
		GridCoverage2D digitalElevationModel = DEMreader.outRaster;
              
		String inPathToTemperature 				="resources/Input/dataET_point/Cavone/1/airT_1.csv";
        String inPathToWind 					="resources/Input/dataET_point/Cavone/1/Wind_1.csv";
        String inPathToRelativeHumidity 		="resources/Input/dataET_point/Cavone/1/RH_1.csv";
        String inPathToShortWaveRadiationDirect ="resources/Input/dataET_point/Cavone/1/ShortwaveDirect_1.csv";
        String inPathToShortWaveRadiationDiffuse="resources/Input/dataET_point/Cavone/1/ShortwaveDiffuse_1.csv";
        String inPathToLWRad 					="resources/Input/dataET_point/Cavone/1/LongDownwelling_1.csv";
        String inPathToNetRad 					="resources/Input/dataET_point/Cavone/1/Net_1.csv";
        String inPathToSoilHeatFlux 			="resources/Input/dataET_point/Cavone/1/GHF_1.csv";
        String inPathToPressure 				="resources/Input/dataET_point/Cavone/1/Pres_1.csv";
        String inPathToLai 						="resources/Input/dataET_point/Cavone/1/LAI_1.csv";
        String inPathToCentroids 				="resources/Input/dataET_point/Cavone/1/centroids_ID_1.shp";
        
        String outPathToLatentHeatSun			="resources/Output/LysProspero/LatentHeatSun.csv";
        String outPathToLatentHeatShadow		="resources/Output/LysProspero/LatentHeatShadow.csv";
        
        String outPathToFluxTranspiration		="resources/Output/LysProspero/FluxTranspiration.csv";
        String outPathToFluxEvapoTranspiration	="resources/Output/LysProspero/FluxEvapoTranspiration.csv";
        String outPathToEvapoTranspiration		="resources/Output/LysProspero/EvapoTranspiration.csv";
        
		String outPathToLeafTemperatureSun		="resources/Output/LysProspero/LeafTemperatureSun.csv";
		String outPathToLeafTemperatureShadow	="resources/Output/LysProspero/LeafTemperatureSh.csv";
		String outPathToSensibleSun				="resources/Output/LysProspero/sensibleSun.csv";
		String outPathToSensibleShadow			="resources/Output/LysProspero/sensibleShadow.csv";
        String outPathToSoilEvaporation 		="resources/Output/LysProspero/Evaporation.csv";
		String outPathToSun						="resources/Output/LysProspero/RadSun.csv";
		String outPathToShadow					="resources/Output/LysProspero/RadShadow.csv";
		String outPathToCanopy					="resources/Output/LysProspero/Canopy.csv";
	
        OmsTimeSeriesIteratorReader temperatureReader	= getTimeseriesReader(inPathToTemperature, fId, startDate, endDate, timeStepMinutes);
        OmsTimeSeriesIteratorReader windReader 		 	= getTimeseriesReader(inPathToWind, fId, startDate, endDate, timeStepMinutes);
        OmsTimeSeriesIteratorReader humidityReader 		= getTimeseriesReader(inPathToRelativeHumidity, fId, startDate, endDate, timeStepMinutes);
        OmsTimeSeriesIteratorReader shortwaveReaderDirect 	= getTimeseriesReader(inPathToShortWaveRadiationDirect, fId, startDate, endDate,timeStepMinutes);
        OmsTimeSeriesIteratorReader shortwaveReaderDiffuse 	= getTimeseriesReader(inPathToShortWaveRadiationDiffuse, fId, startDate, endDate,timeStepMinutes);
        OmsTimeSeriesIteratorReader longwaveReader 		= getTimeseriesReader(inPathToLWRad, fId, startDate, endDate,timeStepMinutes);
        OmsTimeSeriesIteratorReader pressureReader 		= getTimeseriesReader(inPathToPressure, fId, startDate, endDate,timeStepMinutes);
        OmsTimeSeriesIteratorReader leafAreaIndexReader	= getTimeseriesReader(inPathToLai, fId, startDate, endDate,timeStepMinutes);
        OmsTimeSeriesIteratorReader soilHeatFluxReader 	= getTimeseriesReader(inPathToSoilHeatFlux, fId, startDate, endDate,timeStepMinutes);
        OmsTimeSeriesIteratorReader netRadReader 	= getTimeseriesReader(inPathToNetRad, fId, startDate, endDate,timeStepMinutes);

        OmsShapefileFeatureReader centroidsReader 		= new OmsShapefileFeatureReader();
        centroidsReader.file = inPathToCentroids;
		centroidsReader.readFeatureCollection();
		SimpleFeatureCollection stationsFC = centroidsReader.geodata;
		
		OmsTimeSeriesIteratorWriter latentHeatSunWriter = new OmsTimeSeriesIteratorWriter();
		latentHeatSunWriter.file = outPathToLatentHeatSun;
		latentHeatSunWriter.tStart = startDate;
		latentHeatSunWriter.tTimestep = timeStepMinutes;
		latentHeatSunWriter.fileNovalue="-9999";
		
		OmsTimeSeriesIteratorWriter latentHeatShadowWriter = new OmsTimeSeriesIteratorWriter();
		latentHeatShadowWriter.file = outPathToLatentHeatShadow;
		latentHeatShadowWriter.tStart = startDate;
		latentHeatShadowWriter.tTimestep = timeStepMinutes;
		latentHeatShadowWriter.fileNovalue="-9999";
		
		OmsTimeSeriesIteratorWriter FluxTranspirationWriter = new OmsTimeSeriesIteratorWriter();
		FluxTranspirationWriter.file = outPathToFluxTranspiration;
		FluxTranspirationWriter.tStart = startDate;
		FluxTranspirationWriter.tTimestep = timeStepMinutes;
		FluxTranspirationWriter.fileNovalue="-9999";
		
		OmsTimeSeriesIteratorWriter FluxEvapoTranspirationWriter = new OmsTimeSeriesIteratorWriter();
		FluxEvapoTranspirationWriter.file = outPathToFluxEvapoTranspiration;
		FluxEvapoTranspirationWriter.tStart = startDate;
		FluxEvapoTranspirationWriter.tTimestep = timeStepMinutes;
		FluxEvapoTranspirationWriter.fileNovalue="-9999";

		OmsTimeSeriesIteratorWriter EvapoTranspirationWriter = new OmsTimeSeriesIteratorWriter();
		EvapoTranspirationWriter.file = outPathToEvapoTranspiration;
		EvapoTranspirationWriter.tStart = startDate;
		EvapoTranspirationWriter.tTimestep = timeStepMinutes;
		EvapoTranspirationWriter.fileNovalue="-9999";
		
		OmsTimeSeriesIteratorWriter leafTemperatureSunWriter = new OmsTimeSeriesIteratorWriter();
		leafTemperatureSunWriter.file = outPathToLeafTemperatureSun;
		leafTemperatureSunWriter.tStart = startDate;
		leafTemperatureSunWriter.tTimestep = timeStepMinutes;
		leafTemperatureSunWriter.fileNovalue="-9999";
		
		OmsTimeSeriesIteratorWriter leafTemperatureShadowWriter = new OmsTimeSeriesIteratorWriter();
		leafTemperatureShadowWriter.file = outPathToLeafTemperatureShadow;
		leafTemperatureShadowWriter.tStart = startDate;
		leafTemperatureShadowWriter.tTimestep = timeStepMinutes;
		leafTemperatureShadowWriter.fileNovalue="-9999";
		
		OmsTimeSeriesIteratorWriter radiationSunWriter = new OmsTimeSeriesIteratorWriter();
		radiationSunWriter.file = outPathToSun;
		radiationSunWriter.tStart = startDate;
		radiationSunWriter.tTimestep = timeStepMinutes;
		radiationSunWriter.fileNovalue="-9999";
		
		OmsTimeSeriesIteratorWriter radiationShadowWriter = new OmsTimeSeriesIteratorWriter();
		radiationShadowWriter.file = outPathToShadow;
		radiationShadowWriter.tStart = startDate;
		radiationShadowWriter.tTimestep = timeStepMinutes;
		radiationShadowWriter.fileNovalue="-9999";
		
		OmsTimeSeriesIteratorWriter sensibleSunWriter = new OmsTimeSeriesIteratorWriter();
		sensibleSunWriter.file = outPathToSensibleSun;
		sensibleSunWriter.tStart = startDate;
		sensibleSunWriter.tTimestep = timeStepMinutes;
		sensibleSunWriter.fileNovalue="-9999";
		
		OmsTimeSeriesIteratorWriter sensibleShadowWriter = new OmsTimeSeriesIteratorWriter();
		sensibleShadowWriter.file = outPathToSensibleShadow;
		sensibleShadowWriter.tStart = startDate;
		sensibleShadowWriter.tTimestep = timeStepMinutes;
		sensibleShadowWriter.fileNovalue="-9999";
		
	
		OmsTimeSeriesIteratorWriter evaporationWriter = new OmsTimeSeriesIteratorWriter();
		evaporationWriter.file = outPathToSoilEvaporation;
		evaporationWriter.tStart = startDate;
		evaporationWriter.tTimestep = timeStepMinutes;
		evaporationWriter.fileNovalue="-9999";
		
		OmsTimeSeriesIteratorWriter canopyWriter = new OmsTimeSeriesIteratorWriter();
		canopyWriter.file = outPathToCanopy;
		canopyWriter.tStart = startDate;
		canopyWriter.tTimestep = timeStepMinutes;
		canopyWriter.fileNovalue="-9999";
		
		OmsLysProspero Prospero= new OmsLysProspero();
		Prospero.inCentroids = stationsFC;
		Prospero.idCentroids="ID";
		Prospero.centroidElevation="pitfiller";
		
		Prospero.inDem = digitalElevationModel; 


		//Prospero.elevation = 1556;
		//Prospero.latitude = 46.015966;
		//Prospero.longitude = 11.045879;
		Prospero.canopyHeight = 0.2;
		Prospero.defaultStress = 1.0;
		Prospero.doIterative = false;
		
		
		Prospero.useRadiationStress=true;
		Prospero.useTemperatureStress=true;
		Prospero.useVDPStress=true;
		
		
		Prospero.useWaterStress=false;
		Prospero.defaultWaterStress = 1;
		Prospero.stressWater = 0.9259259259259265;
		
		Prospero.alpha = 0.005;
		Prospero.theta = 0.9;
		Prospero.VPD0 = 5.0;
        	
		Prospero.Tl = -5.0;
		Prospero.T0 = 20.0;
		Prospero.Th = 45.0;
		Prospero.typeOfCanopy="multilayer";
		//Prospero.waterWiltingPoint = 0.15;
		//Prospero.waterFieldCapacity = 0.27; 
		//Prospero.rootsDepth = 0.75;
		//Prospero.depletionFraction = 0.55;        
		
		while(temperatureReader.doProcess ) {
        	temperatureReader.nextRecord();
        	
       		

            HashMap<Integer, double[]> id2ValueMap = temperatureReader.outData;
            Prospero.inAirTemperature = id2ValueMap;
            Prospero.doHourly = true;
            Prospero.doFullPrint = false;
           //Prospero.typeOfTerrainCover = "FlatSurface";
            Prospero.tStartDate = startDate;
            Prospero.temporalStep = timeStepMinutes;

            windReader.nextRecord();
            id2ValueMap = windReader.outData;
            Prospero.inWindVelocity = id2ValueMap;

            humidityReader.nextRecord();
            id2ValueMap = humidityReader.outData;
            Prospero.inRelativeHumidity = id2ValueMap;

            shortwaveReaderDirect.nextRecord();
            id2ValueMap = shortwaveReaderDirect.outData;
            Prospero.inShortWaveRadiationDirect = id2ValueMap;
            
            shortwaveReaderDiffuse.nextRecord();
            id2ValueMap = shortwaveReaderDiffuse.outData;
            Prospero.inShortWaveRadiationDiffuse = id2ValueMap;
            
            longwaveReader.nextRecord();
            id2ValueMap = longwaveReader.outData;
            Prospero.inLongWaveRadiation = id2ValueMap;
            
            soilHeatFluxReader.nextRecord();
            id2ValueMap = soilHeatFluxReader.outData;
            Prospero.inSoilFlux = id2ValueMap;
            
            pressureReader.nextRecord();
            id2ValueMap = pressureReader.outData;
            Prospero.inAtmosphericPressure = id2ValueMap;
            
            leafAreaIndexReader.nextRecord();
            id2ValueMap = leafAreaIndexReader.outData;
            Prospero.inLeafAreaIndex = id2ValueMap;
            
            netRadReader.nextRecord();
            id2ValueMap = netRadReader.outData;
            Prospero.inNetLongWaveRadiation = id2ValueMap;
            
           // inPathToNetRad
            Prospero.pm = pm;
            Prospero.process();

            latentHeatSunWriter.inData = Prospero.outLatentHeat;
            latentHeatSunWriter.writeNextLine();

			latentHeatShadowWriter.inData = Prospero.outLatentHeatShade;
            latentHeatShadowWriter.writeNextLine();		
            
            FluxTranspirationWriter.inData = Prospero.outFluxTranspiration;
            FluxTranspirationWriter.writeNextLine();

            FluxEvapoTranspirationWriter.inData = Prospero.outFluxEvapoTranspiration;
            FluxEvapoTranspirationWriter.writeNextLine();	
			
			EvapoTranspirationWriter.inData = Prospero.outEvapoTranspiration;
			EvapoTranspirationWriter.writeNextLine();

			evaporationWriter.inData = Prospero.outEvaporation;
			evaporationWriter.writeNextLine();
			
			if (Prospero.doFullPrint == true) {
			leafTemperatureSunWriter.inData = Prospero.outLeafTemperature;
			leafTemperatureSunWriter.writeNextLine();			 	

			leafTemperatureShadowWriter.inData = Prospero.outLeafTemperatureShade;
			leafTemperatureShadowWriter.writeNextLine();			 	

			radiationSunWriter.inData = Prospero.outRadiation;
			radiationSunWriter.writeNextLine();			 	

			radiationShadowWriter.inData = Prospero.outRadiationShade;
			radiationShadowWriter.writeNextLine();			 	
			
			sensibleSunWriter.inData = Prospero.outSensibleHeat;
			sensibleSunWriter.writeNextLine();			 	
			
			sensibleShadowWriter.inData = Prospero.outSensibleHeatShade;
			sensibleShadowWriter.writeNextLine();	
			
			canopyWriter.inData = Prospero.outCanopy;
			canopyWriter.writeNextLine();
			
			leafTemperatureSunWriter.close();
			leafTemperatureShadowWriter.close();
			radiationSunWriter.close();
			radiationShadowWriter.close();
			sensibleSunWriter.close();
			sensibleShadowWriter.close();
			evaporationWriter.close();
			canopyWriter.close();
			}
	        }
       
        temperatureReader.close();        
        windReader.close();
        humidityReader.close();     
        shortwaveReaderDirect.close();
        shortwaveReaderDiffuse.close();
        longwaveReader.close();
        soilHeatFluxReader.close();
        pressureReader.close();
        leafAreaIndexReader.close();
                
        latentHeatSunWriter.close();
		latentHeatShadowWriter.close();
		FluxEvapoTranspirationWriter.close();
		FluxTranspirationWriter.close();
		EvapoTranspirationWriter.close();



    }

    private OmsTimeSeriesIteratorReader getTimeseriesReader( String path, String id, String startDate, String endDate,
            int timeStepMinutes ) throws URISyntaxException {
        OmsTimeSeriesIteratorReader reader = new OmsTimeSeriesIteratorReader();
        reader.file = path;
        reader.idfield = id;
        reader.tStart =startDate;
        reader.tTimestep = timeStepMinutes;
        reader.tEnd = endDate;
        reader.fileNovalue = "-9999.0";
        reader.initProcess();
        return reader;
    }

}
