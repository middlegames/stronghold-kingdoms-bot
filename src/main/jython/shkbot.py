##
# This is the first version of SHK Bot I wrote in Aptos, CA
##

shield = Region(0,24,78,88)
system = Region(564,0,460,78)
tabs = Region(555,75,469,46)
controls = Region(559,114,465,31)	
barter = Region(79,211,304,61)
resources = Region(80,340,188,317)
sellarea = Region(643,390,335,185)
buyarea = Region(644,194,334,183)
map = Region(0,145,825,593)
device = Region(824,145,200,592)
villages = Region(612,75,402,277)

shield.setThrowException(False)
system.setThrowException(False)
tabs.setThrowException(False)
controls.setThrowException(False)
barter.setThrowException(False)
resources.setThrowException(False)
sellarea.setThrowException(False)
buyarea.setThrowException(False)
map.setThrowException(False)
device.setThrowException(False)
villages.setThrowException(False)

class Perish:
	my1 = "perish-my1.png"
	my2 = "perish-my2.png"


##
# Check if SK is open and user is logged in.
def isSKOpen():
	return shield.exists("1313130430823.png")

##
# Open country map tab.
def openMap():
	map1 = "control-map-1.png"
	map2 = "control-map-2.png"
	if tabs.exists(map1):
		tabs.click(map1)
	else:
		if tabs.exists(map2):
			tabs.click(map2)
		else:
			popup("Willage tab has not been found!");


##
# Open willage tab.
def openWillage():
	hut1 = "control-village-1.png"
	hut2 = "control-village-2.png"
	if tabs.exists(hut1): 
		tabs.click(hut1)
	else:
		if exists(hut2):
			click(hut2)
		else:
			popup("Cannot open Willage tab!");

##
# Open marchant tab. This function will open willage tab 
# each time to be sure that process is running in 
# correct tab.
def openMarchant():
	openWillage()
	marchant1 = "control-marchant-1.png"
	marchant2 = "control-marchant-2.png"
	if controls.exists(marchant1):
		controls.click(marchant1)
	else:
		if controls.exists(marchant2):
			controls.click(marchant2)
		else:
			popup("Cannot open Marchant control!");

def centerVillage(village):
	if not isSKOpen():
		popup("SK has to be open to interact")
	else:
		dropdown = system.find("system-villages-switcher.png").right() 
		system.click(dropdown)
		sleep(1)
		villages.click(village)
		sleep(4)

class Marchant:
	@staticmethod
	def sell(material):
		stock = material[0]
		# XXX fix my name
		Resources = material[1]
		openMarchant()
		handler = ["1313129104198.png","l.png","1313206565653.png"]
		barter.wait(stock, 10)
		if not barter.exists(stock):
			popup("Stockpile button cannot be found!")
			return False
		else:
			barter.click(stock)
			resources.click(Resources)
		sell = False
		sellbutton = "Sell-4.png"
		if sellarea.exists("text-sell.png") and sellarea.exists(sellbutton):
			if not sellarea.exists(handler[0]):
				sellarea.click(handler[1])
			if sellarea.exists(handler[0]) or sellarea.exists(handler[2]):
				sellarea.click("Sell-4.png")
				sell = True
		openMap()
		return sell

##
# This is a set of available food to be traded.
	
class Village:
	sv01 = Pattern("village-1.png").similar(1.00)
	sv02 = Pattern("village-2.png").similar(1.00)
	sv03 = "village-3.png"

##
# This is a set of available stashes to be searched.
class Stash:
	stash     = "stash-new.png"
	apples    = Pattern("stash-apples.png").exact()
	meat      = "stash-meat.png"
	cheese    = "stash-cheese.png"
	bread     = "stash-bread.png"
	fish      = "stash-fish.png"
	veg       = "stash-veg.png"
	ale       = "stash-ale.png"
	wood      = "stash-wood.png"
	stone     = "stash-stone.png"
	iron      = "stash-iron.png"
	pitch     = "stash-pitch.png"
	wine      = "stash-wine.png"
	metalware = "stash-metalware.png"
	clothes   = "stash-clothes.png"
	furniture = "stash-furniture.png"
	salt      = "stash-salt.png"
	silk      = "stash-silk.png"
	spices    = "stash-spices.png"

class Food:
	apples = ["rZr.png","Apples.png","stash-apples.png"]
	ale    = ["category-food.png","Ale.png",Stash.ale]
class Resources:
	wood  = ["category-resources.png","Wood.png",Stash.wood]
	stone = ["1313198104771.png","Stone.png",Stash.stone]
	iron  = ["1313198104771.png","Iron.png",Stash.iron]
	pitch = ["1313198104771.png","Pitch.png"]
class Banqueting:
	furniture = ["1313610958241.png","Furniture.png",Stash.furniture]
	spices    = ["1313610958241.png","Spices.png",Stash.spices]
	silk      = ["category-bankqueting.png","Silk.png",Stash.silk]

class Mouse:
	@staticmethod
	def slowClick(location):
		map.mouseMove(location)
		sleep(0.5)
		map.mouseDown(Button.LEFT)
		sleep(0.5)
		map.mouseUp(Button.LEFT)
		sleep(0.3)

class Scout:
	@staticmethod
	def scout(village, stash):
		openMap()
		centerVillage(village)
		sendscout = Region(102,501,628,115)
		sendscout.setThrowException(False)
		# location = map.find(stash)
		loc = Scout.rsearch(stash, 250)
		if loc:
			Mouse.slowClick(loc)
			device.click("button-scout.png")
			sendscout.wait("icon-scout.png", 5)
			#k = sendscout.find(Pattern("scroll-handle.png").exact());
			#if k:
			#	sendscout.dragDrop(k, [k.x - 100, k.y])
			sendscout.click("G0-1.png")
			sleep(0.5)
			closebutton = "1313209825368.png"
			if map.exists(closebutton):
				map.click(closebutton)
			return True
		return False
	##
	# Search for the stash in given radius (square)
	@staticmethod
	def rsearch(stash, radius):
		c = map.find(stash)
		if c: return c
		t = map.find("village-shield.png.png")
		if not t: return False
		map.dragDrop(t, [t.x - radius, t.y])
		c = map.find(stash)
		if c: return c
		map.dragDrop(t, [t.x, t.y - radius])
		c = map.find(stash)
		if c: return c
		map.dragDrop(t, [t.x + radius, t.y])
		map.dragDrop(t, [t.x + radius, t.y])
		c = map.find(stash)
		if c: return c
		map.dragDrop(t, [t.x, t.y + radius])
		map.dragDrop(t, [t.x, t.y + radius])
		c = map.find(stash)
		if c: return c
		map.dragDrop(t, [t.x - radius, t.y])
		map.dragDrop(t, [t.x - radius, t.y])
		return map.find(stash)

Region(391,244,228,455)Region(391,244,228,455)
	

# --------- START ---------- #

while isSKOpen():
	delay = 30
	goodies = [
		Stash.stash,   # food
		Stash.ale,
		Stash.apples,
		Stash.meat,
		Stash.cheese,
		Stash.bread,
		Stash.fish,
		Stash.veg,
		Stash.wood,    # resources
		Stash.stone,
		Stash.iron,
		Stash.wine,      # luxuries
		Stash.metalware,
		Stash.clothes,
		Stash.furniture,
		Stash.salt,
		Stash.silk,
		Stash.spices]

	sellings = [
		Food.apples,
		Food.ale,
		Banqueting.spices,
		#Banqueting.silk,
		Resources.wood,
		Resources.stone,
		Resources.iron]

	villz = [
		Village.sv01,
		Village.sv02,
		Village.sv03]

	for vill in villz: 
		for goodie in goodies:
			if Scout.scout(vill, goodie):
				print("Scout " + goodie)
		for selling in sellings:
			if Marchant.sell(selling):
				print("Sell " + selling[2])

