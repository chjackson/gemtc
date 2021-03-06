regressorData <- function(data, var) {
  studies <- unique(data[['study']])
  covar <- unname(sapply(studies, function(study) {
    sel <- data[['study']] == study
    data[[var]][sel][1]
  }))
  rval <- data.frame(study=studies)
  rval[[var]] <- covar
  rval
}

data.ab <- read.csv('afstroke-data.csv')
treatments <- read.csv('afstroke-treatments.csv')
classes <- list("control"=c(1),
                "anti-coagulant"=c(2,3,4,9),
                "anti-platelet"=c(5,6,7,8,10,11,12,16,17),
                "mixed"=c(13,14,15))

network <- mtc.network(data.ab=data.ab, treatments=treatments, studies=regressorData(data.ab, 'stroke'))

model <- mtc.model(network, type="regression", regressor=list('coefficient'='shared', 'variable'='stroke', 'classes'=classes))
