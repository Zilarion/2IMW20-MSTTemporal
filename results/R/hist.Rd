# Load data
data <- read.csv("./processedfinal.csv")

png(filename=paste("distV", index))
hist(data[,2], col="blue", xlab="|V|", main="", breaks=20)
dev.off();

png(filename=paste("distE", index))
hist(data[,3], col="blue", xlab="|E|", main="", breaks=20)
dev.off();

png(filename=paste("distEV", index))
hist(data[,3]/data[,2], col="blue", xlab="|E|/|V|", main="", breaks=20)
dev.off();