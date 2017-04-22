class PurityReport(object):

    def __init__(self, param_list):
        if (len(param_list) == 7):
            self.date = param_list[0]
            self.reportID = param_list[1]
            self.userID = param_list[2]
            self.address = param_list[3]
            self.condition = param_list[4]
            self.virus = param_list[5]
            self.contaminant = param_list[6]

    @staticmethod
    def param_list():
        return ["reportDate", "reportID", "reporterID", "streetAddress", "waterCondition", "virusPPM", "contaminantPPM"]

    @staticmethod
    def path():
        return "/PurityReports"

    def __str__(self):
        return ("Date: " + str(self.date) + " ReportID: " + str(self.reportID) + " UserID: " + str(self.userID) + " Address: " + str(self.address) + " Condition: " + str(self.condition) + " PPMs: " + str(self.virus) + " " + str(self.contaminant))
