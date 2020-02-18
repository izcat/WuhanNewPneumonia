
class Bed:
	def __init__(self, x, y):
		self.x = x
		self.y = y
		self.empty = True

	def setEmpty(self, bool):
		self.empty = bool






class Hospital:
	def __init__(self):
		pass


def pickBed():
	bed = Bed(1, 2)
	return bed


def returnBed(bed):
	bed.setEmpty(True)