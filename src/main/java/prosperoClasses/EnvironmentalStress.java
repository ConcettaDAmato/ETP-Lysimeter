package prosperoClasses;
/*import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.lang.Math;
import oms3.annotations.Author;
import oms3.annotations.Description;
import oms3.annotations.Execute;
import oms3.annotations.In;
import oms3.annotations.Keywords;
import oms3.annotations.Label;
import oms3.annotations.License;
import oms3.annotations.Name;
import oms3.annotations.Out;
import oms3.annotations.Status;
import oms3.annotations.Unit;
import prosperoClasses.*;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.jgrasstools.gears.libs.modules.JGTModel;*/

public class EnvironmentalStress {
	
	
	public double computeRadiationStress(double shortWaveRadiation, double alpha, double theta) {
//		double shortWaveRadiationMicroMol=(shortWaveRadiation>0)?shortWaveRadiation/2.11:0;
		double shortWaveRadiationMicroMol=(shortWaveRadiation);
		double first = (alpha*shortWaveRadiationMicroMol)+1;

		double sqr1 = Math.pow(first, 2);
		double sqr2 = - 4*theta*alpha*shortWaveRadiationMicroMol;
		double sqr = sqr1+sqr2;
		double result = (1/(2*theta))*(alpha*shortWaveRadiationMicroMol+1-Math.sqrt((sqr))) ;
		return result;	
	}
	
	public double computeTemperatureStress(double airTemperature, double Tl, double Th, double T0) {
		airTemperature = airTemperature -273;
		double c = (Th-T0)/(T0-Tl);
		double b = 1/((T0-Tl)*Math.pow((Th-T0),c));
		double result = b* (airTemperature - Tl)* Math.pow((Th-airTemperature),c);
				
		return result;	
	}
	
	public double computeVapourPressureStress(double vapourPressureDeficit, double VPD0) {
		double result = Math.exp(-vapourPressureDeficit/VPD0);
		return result;	
	}
	
	
	public double computeFAOWaterStress(double soilMoisture, double waterFieldCapacity, 
			double waterWiltingPoint, double rootsDepth, double depletionFraction) {
		double waterStressCoefficient;
		
		if (soilMoisture <= waterWiltingPoint) {
			waterStressCoefficient = 0;}
		
		else if(soilMoisture > waterWiltingPoint && soilMoisture < waterFieldCapacity) {	
			double totalAvailableWater = 1000*(waterFieldCapacity - waterWiltingPoint)*rootsDepth;
			double readilyAvailableWater = totalAvailableWater * depletionFraction;
			double rootZoneDepletation = 1000 * (waterFieldCapacity - soilMoisture) * rootsDepth;
			waterStressCoefficient=(rootZoneDepletation<readilyAvailableWater)? 1:(totalAvailableWater - rootZoneDepletation) / (totalAvailableWater - readilyAvailableWater);}
		
		else {waterStressCoefficient = 1;}
		
		return waterStressCoefficient;	
	}
	
	
	
	public double computeWaterStress(double soilMoisture, double f, double thetaW, double thetaC) {
		double beta;
		if (soilMoisture < thetaW) {
			beta = 0;		
			}
		else if(soilMoisture > thetaW && soilMoisture < thetaC) {
			beta = (soilMoisture - thetaW)/(thetaC - thetaW);
			}
		else {
			beta = 1;
			}
		double result = (1 - Math.exp(-f*beta))/(1 - Math.exp(-f));
		//System.out.println("Beta is:   "+beta);	
		return result;	
	}
}

