class Report(object):

    def __init__(self, param_list):
        if (len(param_list) == 8):
            self.date = param_list[0]
            self.reportID = param_list[1]
            self.userID = param_list[2]
            self.address = param_list[3]
            self.type = param_list[4]
            self.condition = param_list[5]
            self.latitude = param_list[6]
            self.longitude = param_list[7]

    @staticmethod
    def param_list():
        return ["reportDate", "reportID", "reporterID", "streetAddress", "waterType", "waterCondition", "latitude", "longitude"]

    @staticmethod
    def path():
        return "/Reports"

    def __str__(self):
        return ("Date: " + str(self.date) + " ReportID: " + str(self.reportID) + " UserID: " + str(self.userID) + " Address: " + str(self.address) + " Type: " + str(self.type) + " Condition: " + str(self.condition) + " LatLng: " + str(self.latitude) + str(self.longitude))
