#ifndef ITEM_H
#define ITEM_H

#include <iostream>
using std::ostream;
using std::ostream;
using std::cout;

#include <string>
using std::string;

#include <cmath>
using std::pow;
using std::cos;
using std::sin;
using std::atan2;

// TODO: your code  goes here

class Item {

protected:
	double latitude;
	double longitude;
	string ID;
	int time;

public:
	Item(double latitudeC, double longitudeC, string IDC, int timeC) : latitude(latitudeC), longitude(longitudeC), ID(IDC), time(timeC) {
	}

	// getters
	double getLat() {
		return latitude;
	}

	double getLon() {
		return longitude;
	}

	string getID() {
		return ID;
	}

	int getTime() {
		return time;
	}

	double distanceTo(Item item2) {
		double pi = 3.1415926535897; // could have used M_PI
		longitude = (longitude * pi) / 180;
		latitude = (latitude * pi) / 180;
		item2.longitude = (item2.longitude * pi) / 180;
		item2.latitude = (item2.latitude * pi) / 180;

		double dlon = item2.longitude - longitude;
		double dlat = item2.latitude - latitude;

		double a = pow((sin(dlat / 2)), 2) + cos(latitude) * cos(item2.latitude) * pow((sin(dlon / 2)), 2);
		double c = 2 * atan2(sqrt(a), sqrt(1 - a));
		double distance = 6373000 * c;

		return distance;
	}
};

ostream& operator<<(ostream& os, Item& item) {
	os << "{" << item.getLat() << ", " << item.getLon() << ", \"" << item.getID() << "\", " << item.getTime() << "}";
	return os;
}

// don't write any code below this line

#endif
