PGDMP         %    	            x            db_smsmissedcall    12.2    12.2 '    :           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            ;           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            <           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            =           1262    156449    db_smsmissedcall    DATABASE     �   CREATE DATABASE db_smsmissedcall WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'French_France.1252' LC_CTYPE = 'French_France.1252';
     DROP DATABASE db_smsmissedcall;
                smsmissedcall    false            �            1259    181106 
   callmissed    TABLE     �  CREATE TABLE public.callmissed (
    id bigint NOT NULL,
    code_entreprise character varying(255),
    code_ticket character varying(255),
    date_insertion timestamp without time zone,
    date_modification timestamp without time zone,
    date_heure_alerte character varying(255),
    date_heure_decroche character varying(255),
    dateheuralerte character varying(255),
    destinataire character varying(255),
    duree character varying(255),
    etat integer NOT NULL,
    ligne_direct character varying(255),
    nom character varying(255),
    poste character varying(255),
    profil character varying(255),
    standard character varying(255),
    traite integer NOT NULL
);
    DROP TABLE public.callmissed;
       public         heap    smsmissedcall    false            �            1259    181104    callmissed_id_seq    SEQUENCE     z   CREATE SEQUENCE public.callmissed_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.callmissed_id_seq;
       public          smsmissedcall    false    211            >           0    0    callmissed_id_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE public.callmissed_id_seq OWNED BY public.callmissed.id;
          public          smsmissedcall    false    210            �            1259    172854 	   modelesms    TABLE       CREATE TABLE public.modelesms (
    id bigint NOT NULL,
    delai character varying(255),
    initial character varying(255),
    sms_appel_entrant character varying(255),
    sms_appel_sortant_ld character varying(255),
    sms_appel_sortant_sld character varying(255)
);
    DROP TABLE public.modelesms;
       public         heap    smsmissedcall    false            �            1259    172852    modelesms_id_seq    SEQUENCE     y   CREATE SEQUENCE public.modelesms_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.modelesms_id_seq;
       public          smsmissedcall    false    205            ?           0    0    modelesms_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.modelesms_id_seq OWNED BY public.modelesms.id;
          public          smsmissedcall    false    204            �            1259    181055    paramapi    TABLE     �  CREATE TABLE public.paramapi (
    id bigint NOT NULL,
    date_modification timestamp without time zone,
    etat integer NOT NULL,
    flash character varying(255),
    method_bulk character varying(255),
    method_full character varying(255),
    method_one character varying(255),
    sender character varying(255),
    token character varying(255),
    url character varying(255),
    username character varying(255),
    titre character varying(255),
    title character varying(255)
);
    DROP TABLE public.paramapi;
       public         heap    smsmissedcall    false            �            1259    181053    paramapi_id_seq    SEQUENCE     x   CREATE SEQUENCE public.paramapi_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.paramapi_id_seq;
       public          smsmissedcall    false    209            @           0    0    paramapi_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.paramapi_id_seq OWNED BY public.paramapi.id;
          public          smsmissedcall    false    208            �            1259    172899    paramsqlserver    TABLE     �  CREATE TABLE public.paramsqlserver (
    id bigint NOT NULL,
    date_insertion timestamp without time zone,
    date_modification timestamp without time zone,
    dbname character varying(255),
    driver character varying(255),
    etat integer NOT NULL,
    host character varying(255),
    password character varying(255),
    port integer NOT NULL,
    username character varying(255),
    delai character varying(255)
);
 "   DROP TABLE public.paramsqlserver;
       public         heap    smsmissedcall    false            �            1259    172897    paramsqlserver_id_seq    SEQUENCE     ~   CREATE SEQUENCE public.paramsqlserver_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.paramsqlserver_id_seq;
       public          smsmissedcall    false    207            A           0    0    paramsqlserver_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.paramsqlserver_id_seq OWNED BY public.paramsqlserver.id;
          public          smsmissedcall    false    206            �            1259    164638    role    TABLE     �   CREATE TABLE public.role (
    role character varying(255) NOT NULL,
    description character varying(255),
    etat integer NOT NULL
);
    DROP TABLE public.role;
       public         heap    smsmissedcall    false            �            1259    164646    utilisateur    TABLE     �  CREATE TABLE public.utilisateur (
    username character varying(255) NOT NULL,
    civilite character varying(255),
    date_creation timestamp without time zone,
    date_modification timestamp without time zone,
    email character varying(255),
    etat integer NOT NULL,
    nom character varying(255),
    password character varying(255),
    prenoms character varying(255),
    role character varying(255)
);
    DROP TABLE public.utilisateur;
       public         heap    smsmissedcall    false            �
           2604    181109    callmissed id    DEFAULT     n   ALTER TABLE ONLY public.callmissed ALTER COLUMN id SET DEFAULT nextval('public.callmissed_id_seq'::regclass);
 <   ALTER TABLE public.callmissed ALTER COLUMN id DROP DEFAULT;
       public          smsmissedcall    false    211    210    211            �
           2604    172857    modelesms id    DEFAULT     l   ALTER TABLE ONLY public.modelesms ALTER COLUMN id SET DEFAULT nextval('public.modelesms_id_seq'::regclass);
 ;   ALTER TABLE public.modelesms ALTER COLUMN id DROP DEFAULT;
       public          smsmissedcall    false    205    204    205            �
           2604    181058    paramapi id    DEFAULT     j   ALTER TABLE ONLY public.paramapi ALTER COLUMN id SET DEFAULT nextval('public.paramapi_id_seq'::regclass);
 :   ALTER TABLE public.paramapi ALTER COLUMN id DROP DEFAULT;
       public          smsmissedcall    false    209    208    209            �
           2604    172902    paramsqlserver id    DEFAULT     v   ALTER TABLE ONLY public.paramsqlserver ALTER COLUMN id SET DEFAULT nextval('public.paramsqlserver_id_seq'::regclass);
 @   ALTER TABLE public.paramsqlserver ALTER COLUMN id DROP DEFAULT;
       public          smsmissedcall    false    207    206    207            7          0    181106 
   callmissed 
   TABLE DATA           �   COPY public.callmissed (id, code_entreprise, code_ticket, date_insertion, date_modification, date_heure_alerte, date_heure_decroche, dateheuralerte, destinataire, duree, etat, ligne_direct, nom, poste, profil, standard, traite) FROM stdin;
    public          smsmissedcall    false    211   2       1          0    172854 	   modelesms 
   TABLE DATA           w   COPY public.modelesms (id, delai, initial, sms_appel_entrant, sms_appel_sortant_ld, sms_appel_sortant_sld) FROM stdin;
    public          smsmissedcall    false    205   �2       5          0    181055    paramapi 
   TABLE DATA           �   COPY public.paramapi (id, date_modification, etat, flash, method_bulk, method_full, method_one, sender, token, url, username, titre, title) FROM stdin;
    public          smsmissedcall    false    209   f3       3          0    172899    paramsqlserver 
   TABLE DATA           �   COPY public.paramsqlserver (id, date_insertion, date_modification, dbname, driver, etat, host, password, port, username, delai) FROM stdin;
    public          smsmissedcall    false    207   4       .          0    164638    role 
   TABLE DATA           7   COPY public.role (role, description, etat) FROM stdin;
    public          smsmissedcall    false    202   �4       /          0    164646    utilisateur 
   TABLE DATA           �   COPY public.utilisateur (username, civilite, date_creation, date_modification, email, etat, nom, password, prenoms, role) FROM stdin;
    public          smsmissedcall    false    203   �4       B           0    0    callmissed_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.callmissed_id_seq', 2, true);
          public          smsmissedcall    false    210            C           0    0    modelesms_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.modelesms_id_seq', 1, true);
          public          smsmissedcall    false    204            D           0    0    paramapi_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.paramapi_id_seq', 1, true);
          public          smsmissedcall    false    208            E           0    0    paramsqlserver_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.paramsqlserver_id_seq', 1, true);
          public          smsmissedcall    false    206            �
           2606    181114    callmissed callmissed_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.callmissed
    ADD CONSTRAINT callmissed_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.callmissed DROP CONSTRAINT callmissed_pkey;
       public            smsmissedcall    false    211            �
           2606    172862    modelesms modelesms_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.modelesms
    ADD CONSTRAINT modelesms_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.modelesms DROP CONSTRAINT modelesms_pkey;
       public            smsmissedcall    false    205            �
           2606    181063    paramapi paramapi_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.paramapi
    ADD CONSTRAINT paramapi_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.paramapi DROP CONSTRAINT paramapi_pkey;
       public            smsmissedcall    false    209            �
           2606    172907 "   paramsqlserver paramsqlserver_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.paramsqlserver
    ADD CONSTRAINT paramsqlserver_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.paramsqlserver DROP CONSTRAINT paramsqlserver_pkey;
       public            smsmissedcall    false    207            �
           2606    164645    role role_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (role);
 8   ALTER TABLE ONLY public.role DROP CONSTRAINT role_pkey;
       public            smsmissedcall    false    202            �
           2606    164653    utilisateur utilisateur_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_pkey PRIMARY KEY (username);
 F   ALTER TABLE ONLY public.utilisateur DROP CONSTRAINT utilisateur_pkey;
       public            smsmissedcall    false    203            �
           2606    164654 '   utilisateur fk8blobjcwfdr8smc5poa1isjar    FK CONSTRAINT     �   ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT fk8blobjcwfdr8smc5poa1isjar FOREIGN KEY (role) REFERENCES public.role(role);
 Q   ALTER TABLE ONLY public.utilisateur DROP CONSTRAINT fk8blobjcwfdr8smc5poa1isjar;
       public          smsmissedcall    false    202    2724    203            7   �   x��λ
�@�z���efv����E�ࣈ��&6���B�B�0�6��;�m�B��թ��]����d��~���}L�H2�s�bv{��۳"���t
0��l��։!@ `bm�6ˠ�!�͠��U���>��z�-JP�j�u���)���>j/(�����(��.�M�      1   �   x���1�0Й���P{�|U�Ґ:q ��F��SG��?ɏ�q�4�Yq�B�IIqΈ�։c�t**��*�<����T[�X��'���Qk�V$���~��/ArD!��1�|0�?�2�/��g��P�n�?S�k�Cn�s_�cM�      5   �   x�-ƻ�0 �����-�B7%`x���	�P�oHL�p�h���F����0�u�>@Ms�����l$���ti��TS�պ~q:����8�����.�����t)��Ms�?�V��9_�}_�a�O��ezh���An{��P�����E�62�      3   |   x�˱
�0�����@������Q�ҵKL"F����[���?�rK�::w,�A�@G��-I5�r_��[��\Q�Yc�ņ���8O�y絥����v����BI��_���A0�Y����&-      .   !   x�KL����,.)J,I--�tD�r��qqq ��      /   �   x�KL�������+�L--�4202�5"cCS++�b)�e��E��ɩ���z������.�e�*F�*�*!�����NAI)��!&f&���z%�y��&�!ɞNn�&yᖞF��Q�!��Q������ 'e�%� ������ GS0y     