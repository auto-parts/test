CREATE TABLE public."OE"
(
   "OE" character varying(20), 
   "OEList" bit(1024)[], 
   CONSTRAINT "OE_PK" PRIMARY KEY ("OE")
) 
WITH (
  OIDS = FALSE
)
;


CREATE TABLE "OE_PHASE_1"
(
  "ID" integer NOT NULL,
  "OE_LIST" character varying(20000),
  CONSTRAINT "OE_PHASE_1_PK" PRIMARY KEY ("ID")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "OE_PHASE_1"
  OWNER TO postgres;
  
  
 CREATE TABLE public."ALLEGRO_INZERATS"
(
   "ID" bigint, 
   "USER_TEXT" character varying(50000)[], 
   "LINK" character varying(100)[], 
   "PRICE" double precision[], 
   "PICTURE" bit varying(1024)[], 
   CONSTRAINT "ALLEGRO_INZERATS_PK" PRIMARY KEY ("ID")
) 
WITH (
  OIDS = FALSE
)
;

