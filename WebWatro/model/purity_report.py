from .watro_object import WatroObject
class PurityReport(WatroObject):
    _field_names = ["reportDate", "reportID", "reporterID", "streetAddress", "waterCondition", "virusPPM", "contaminantPPM"]

    @staticmethod
    def path():
        return "/PurityReports"

    @property
    def id(self):
        return self.reportID