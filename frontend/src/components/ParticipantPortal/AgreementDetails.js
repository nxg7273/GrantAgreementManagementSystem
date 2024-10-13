import React, { useState, useEffect } from 'react';
import { Box, Heading, Text, VStack, HStack, Button, useToast } from '@chakra-ui/react';
import axios from 'axios';

const AgreementDetails = ({ agreementId }) => {
  const [agreement, setAgreement] = useState(null);
  const toast = useToast();

  useEffect(() => {
    fetchAgreementDetails();
  }, [agreementId]);

  const fetchAgreementDetails = async () => {
    try {
      const response = await axios.get(`/api/agreements/${agreementId}`);
      setAgreement(response.data);
    } catch (error) {
      console.error('Error fetching agreement details:', error);
      toast({
        title: 'Error',
        description: 'Failed to fetch agreement details',
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  const handleAccept = async () => {
    try {
      await axios.post(`/api/agreements/${agreementId}/accept`);
      toast({
        title: 'Agreement Accepted',
        description: 'You have successfully accepted the agreement',
        status: 'success',
        duration: 5000,
        isClosable: true,
      });
      fetchAgreementDetails(); // Refresh agreement details
    } catch (error) {
      console.error('Error accepting agreement:', error);
      toast({
        title: 'Error',
        description: 'Failed to accept agreement',
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  const handleReject = async () => {
    try {
      await axios.post(`/api/agreements/${agreementId}/reject`);
      toast({
        title: 'Agreement Rejected',
        description: 'You have rejected the agreement',
        status: 'info',
        duration: 5000,
        isClosable: true,
      });
      fetchAgreementDetails(); // Refresh agreement details
    } catch (error) {
      console.error('Error rejecting agreement:', error);
      toast({
        title: 'Error',
        description: 'Failed to reject agreement',
        status: 'error',
        duration: 5000,
        isClosable: true,
      });
    }
  };

  if (!agreement) {
    return <Box>Loading agreement details...</Box>;
  }

  return (
    <Box p={5}>
      <Heading mb={6}>{agreement.name}</Heading>
      <VStack align="start" spacing={4}>
        <Box>
          <Text fontWeight="bold">Status:</Text>
          <Text>{agreement.status}</Text>
        </Box>
        <Box>
          <Text fontWeight="bold">Grant Details:</Text>
          <Text>{agreement.grantDetails}</Text>
        </Box>
        <Box>
          <Text fontWeight="bold">Agreement Text:</Text>
          <Text>{agreement.legalText}</Text>
        </Box>
        {agreement.status === 'PENDING' && (
          <HStack spacing={4}>
            <Button colorScheme="green" onClick={handleAccept}>
              Accept Agreement
            </Button>
            <Button colorScheme="red" onClick={handleReject}>
              Reject Agreement
            </Button>
          </HStack>
        )}
      </VStack>
    </Box>
  );
};

export default AgreementDetails;
