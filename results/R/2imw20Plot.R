# Load data
data <- read.csv("./processed.csv");


# setup window
par(mfrow=c(2,2))


# Plot graphs for algorithm running time
for (index in c(1, 2)) {
	#preprocess data by removing i == 1 by setting them into old/new sets
	new <- data[data$Algorithm!="Old",]
	old <- data[data$Algorithm!="New",]
	old <- old[old$i==index,]
	new <- new[new$i==index,]

	# sort by closure |V|
	#old <- old[with(old, order(X.V..closure)),]
	#new <- new[with(new, order(X.V..closure)),]
	
	#old[7] and new[7] for normal time, old[9] and new[9] for sum time (includes post-P)
	all <- data.frame(old[2], old[3], old[9], new[9]);

	all <- data.frame(all[1], all[2], all[,3:ncol(all)]/1000)

	# x = max |V| closure, y = max time
	xrangeV <- range(0, max(all[1]))
	xrangeEV <- range(min(all[2]/all[1]), max(all[2]/all[1]))
	yrange <- range(0, max(all[3], all[4]))

	colors = c('#00FF00', '#FF0000')

	all <- all[with(all, order(X.V..closure)),]
	
png(filename=paste("V", index))
	# Plot |V| v.s. Time
	matplot(
		xlim=xrangeV, ylim=yrange, 
		type="l", col=colors,
		xlab="|V|", ylab="Time (s)", 
		all[,1], all[,3:ncol(all)]
	)
dev.off()
	
	all <- all[with(all, order(X.E./X.V..closure)),]
	
png(filename=paste("EoverV", index))
	# Plot |E|/|V| v.s. time
	matplot(
		xlim=xrangeEV, ylim=yrange, 
		type="l", col=colors,
		xlab="|E|/|V|", ylab="Time (s)", 
		all[,2]/all[,1], all[,3:ncol(all)]
	)
dev.off()
}