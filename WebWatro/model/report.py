from .watro_object import WatroObject
class Report(WatroObject):
    _field_names = ["reportDate", "reportID", "reporterID", "streetAddress", "waterType", "waterCondition", "latitude", "longitude"]

    @staticmethod
    def path():
        return "/Reports"

    @property
    def id(self):
        return self.reportID